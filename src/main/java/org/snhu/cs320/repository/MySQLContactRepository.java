package org.snhu.cs320.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.snhu.cs320.contact.Contact;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.exception.DuplicateContactException;
import org.snhu.cs320.exception.RepositoryException;
import org.snhu.cs320.validation.Validation;

public final class MySQLContactRepository implements ContactRepository {

    private static final int DUPLICATE_KEY_ERROR = 1062;

    private final DataSource dataSource;

    public MySQLContactRepository(DataSource dataSource) {
        Validation.validateNotNull(dataSource, "dataSource");
        this.dataSource = dataSource;
    }

    @Override
    public void save(Contact contact) {
        Validation.validateNotNull(contact, "contact");

        String sql =
            "INSERT INTO contacts " +
            "(contact_id, first_name, last_name, phone_number, address) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, contact.getId());
            statement.setString(2, contact.getFirstName());
            statement.setString(3, contact.getLastName());
            statement.setString(4, contact.getPhoneNumber());
            statement.setString(5, contact.getAddress());

            statement.executeUpdate();
        } catch (SQLException exception) {
            if (exception.getErrorCode() == DUPLICATE_KEY_ERROR) {
                throw new DuplicateContactException(contact.getId());
            }

            throw new RepositoryException("Unable to save contact.", exception);
        }
    }

    @Override
    public Optional<Contact> findById(String id) {
        Validation.validateId(id);

        String sql =
            "SELECT contact_id, first_name, last_name, phone_number, address " +
            "FROM contacts WHERE contact_id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return Optional.of(mapContact(results));
                }

                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException(
                "Unable to retrieve contact.",
                exception
            );
        }
    }

    @Override
    public List<Contact> findAll() {
        String sql =
            "SELECT contact_id, first_name, last_name, phone_number, address " +
            "FROM contacts";

        List<Contact> contacts = new ArrayList<>();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery()
        ) {
            while (results.next()) {
                contacts.add(mapContact(results));
            }
            return Collections.unmodifiableList(contacts);
        } catch (SQLException exception) {
            throw new RepositoryException(
                "unable to retrieve contacts.",
                exception
            );
        }
    }

    @Override
    public void replace(Contact contact) {
        String sql =
            "UPDATE contacts " +
            "SET first_name = ?, last_name = ?, phone_number = ?, address = ? " +
            "WHERE contact_id = ?";

        Validation.validateNotNull(contact, "contact");
        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPhoneNumber());
            statement.setString(4, contact.getAddress());
            statement.setString(5, contact.getId());

            int matchedRows = statement.executeUpdate();

            if (matchedRows == 0) {
                throw new ContactNotFoundException(contact.getId());
            }
        } catch (SQLException exception) {
            throw new RepositoryException(
                "Unable to update contact.",
                exception
            );
        }
    }

    @Override
    public void deleteById(String id) {
        Validation.validateId(id);

        String sql = "DELETE FROM contacts WHERE contact_id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id);

            int deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                throw new ContactNotFoundException(id);
            }
        } catch (SQLException exception) {
            throw new RepositoryException(
                "Unable to delete contact.",
                exception
            );
        }
    }

    @Override
    public boolean existsById(String id) {
        Validation.validateId(id);

        String sql = "SELECT 1 FROM contacts WHERE contact_id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id);

            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        } catch (SQLException exception) {
            throw new RepositoryException(
                "Unable to check whether contact exists.",
                exception
            );
        }
    }

    private Contact mapContact(ResultSet results) throws SQLException {
        return new Contact(
            results.getString("contact_id"),
            results.getString("first_name"),
            results.getString("last_name"),
            results.getString("phone_number"),
            results.getString("address")
        );
    }
}
