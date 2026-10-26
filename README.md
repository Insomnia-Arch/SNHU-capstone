# Arch | Computer Science ePortfolio

**Southern New Hampshire University**  
**CS 499: Computer Science Capstone | Concentration in Data Analysis**

[Professional Self-Assessment](#professional-self-assessment) | [Code Review](#code-review) | [Artifact and Enhancements](#artifact-and-enhancements) | [Source Code and Downloads](#source-code-and-downloads)

---

## Professional Self-Assessment

### Introduction and Professional Growth

Howdy, I’m Arch. I’ve worked as an analyst for about ten years. When I began the Computer Science program at Southern New Hampshire University in July 2023, I already had professional experience in data analysis. I understood the value of accurate information, but I wanted a stronger understanding of software development so I could build the tools I needed to collect, validate, organize, and store that data. Throughout the program, I developed skills that strengthened my existing work and expanded my ability to build solutions. My capstone demonstrates that growth through software design, algorithms and data structures, database integration, and automated testing.

My concentration in Data Analysis connects directly to my professional interests. I have used Python to clean, validate, and manipulate data, and my statistics coursework helped me understand regression models and relationships between variables. My computer science coursework added another perspective: the quality of an analysis depends heavily on the systems supplying its data. Validation, maintainable code, appropriate data structures, and reliable storage all contribute to trustworthy results.
[Professional assement.docx](https://github.com/user-attachments/files/33940837/Professional.assement.docx)

### Applying My Skills Through the Capstone

For my capstone, I selected a contact-management project originally developed in CS 320: Software Testing, Automation, and Quality Assurance. The original project provided a foundation in input validation, object-oriented programming, and automated testing. It was one of the projects I was most proud of because its relatively simple design used reusable validation logic rather than repeating the same checks throughout the application.

Using the same artifact across all three enhancement categories allowed me to demonstrate how architectural decisions affect later development. Each enhancement built on the previous work, turning a small in-memory service into a more organized application with indexed searching and persistent storage. It also gave me an opportunity to apply abstraction, encapsulation, and interface-based polymorphism to a system with changing requirements.

### Software Design and Engineering

The first enhancement focused on separating responsibilities. I introduced a repository interface, moved storage out of the service, and supplied the repository through the service constructor instead of relying on a shared singleton. I also made contacts immutable and separated editable fields into an update request. These changes improved test isolation and gave the application a more controlled way to update stored information.

This enhancement required the most planning because it established the foundation for the other two. I knew the application would eventually need MySQL storage, and the search functionality introduced in Enhancement Two would need to work with both in-memory and persistent records. Clear interfaces and encapsulation made that possible. When I later added MySQL, the repository abstraction allowed the service to work with a different storage implementation without requiring a complete redesign.

### Algorithms and Data Structures

The algorithms and data structures enhancement required me to choose structures based on the operations the application needed. I implemented a Trie for first-name and last-name prefix searches and a hash-based phone index that supports multiple contacts sharing a number. Maintaining those indexes during additions, updates, and deletions was as important as implementing the searches themselves.

This work strengthened my understanding of algorithmic trade-offs. Indexed lookup introduces additional memory requirements and additional logic to keep the indexes consistent with stored records. I also learned to distinguish theoretical complexity from measured performance rather than assuming an optimization is always faster in practice.

### Databases, Testing, and Security

The database enhancement added persistent MySQL storage through the existing repository interface. I implemented create, read, update, and delete operations with prepared statements, defined database constraints, and configured a dedicated application account with limited permissions. Connection credentials came from environment variables rather than being embedded in source files.

These decisions demonstrate my growing security mindset by addressing input handling, credential management, and database permissions as part of the design. Integration tests checked database operations, persistence across repository instances, and rebuilding the search indexes from stored records. My final recorded test run contained 114 passing tests, with no failures or skipped tests.

### Troubleshooting and Problem Solving

A Java runtime mismatch prevented Gradle from running, while an incorrectly opened project caused VS Code to report package and dependency errors even when compilation succeeded. Resolving these problems taught me to distinguish application defects from build-environment and editor-configuration problems. I became more deliberate about examining error messages and test evidence before changing code.

### Communication and Collaboration

This portfolio also demonstrates the importance of communication and collaboration. Clear interfaces, descriptive exceptions, repeatable setup instructions, and automated tests give another developer a practical starting point for understanding and contributing to a project. Writing about my enhancements required me to explain the problem, justify my decisions, and acknowledge limitations.

Although this capstone was an individual project, these practices support the shared understanding needed in a collaborative development environment. They helped me consider not only whether I understood my own code, but whether another person could understand, test, and extend it. For this project I relied on the feedback that I received from my professor. He pointed out that even though I had changed up my application architecture, I had not created JUnit tests for the coming data structures and that I had to keep in mind to test my Prepared Statement for SQL as well to ensure that they covered edge cases. Without that feedback I would have forgotten to add the 11 tests for SQL that were included in the third enhancement. 

Throughout the process of this development, I had to rely heavily on my ability to research solutions to the structures that I was implementing and this is an area that I would vastly benefit from collaboration. I worked with a friend who has been working as a software developer and QA tester to discuss methods for data abstraction, input validation, and how to enable my program search and test functions with both the existing in-memory database, and when implemented, the MySQL database. As time goes on, I will vastly benefit from my professional connections, as there are always corners of knowledge that require mastery that I won't have. 

### Professional Direction

Completing this program has strengthened my ability to approach technical problems systematically. I can evaluate a design, select appropriate tools, implement a solution, and use testing to assess its behavior. My goal is to apply these skills to data-focused work by developing tools that improve data quality, reduce repetitive processing, and support useful analysis. This portfolio represents both the progress I have made and the foundation I will continue building throughout my career.

[Professional assement.docx](https://github.com/user-attachments/files/33940853/Professional.assement.docx)


---

## Code Review

My code review examines the original CS 320 application and explains the improvements planned for the capstone. It covers the existing functionality, opportunities to improve the design and testing, and the proposed enhancements in software engineering, algorithms and data structures, and databases.

The recording provides the starting point for understanding the changes presented below. The enhancement narratives explain the completed work, lessons learned, and any differences from the original plan.


[Watch the code-review video](https://youtu.be/NsOcsQcQmus)

---

## Artifact and Enhancements

### Original Artifact: CS 320 Contact Management Application

The original Java application managed contacts containing an ID, first name, last name, phone number, and address. It supported adding, updating, and deleting contacts, stored records in memory, and included validation and JUnit tests. I retained this version so visitors can compare the original design with the enhanced application.

[View or download the original artifact](https://github.com/Insomnia-Arch/SNHU-capstone/commit/a91e39cbfe0fc786c5c4f23eec97066a06ea79ae)

### Enhancement One: Software Design and Engineering

I separated contact-management logic from storage through a repository interface, removed the singleton dependency, introduced immutable contact values and explicit update requests, and refactored the tests around public behavior. This established the architecture used by the later enhancements.

**Skills demonstrated:** object-oriented design, abstraction, encapsulation, dependency injection, API design, defensive programming, and automated testing.

[Enhancement One Narrative.docx](https://github.com/user-attachments/files/33940931/Enhancement.One.Narrative.docx) | [View the milestone code](https://github.com/Insomnia-Arch/SNHU-capstone/commit/af997f0946e292c1b95a46b5ae4a91d75cd81e70)

### Enhancement Two: Algorithms and Data Structures

I added name-prefix searching with a Trie, exact phone-number lookup with a hash-based index, and predictable result ordering. The enhancement also addressed maintaining indexes when contacts are added, updated, or deleted and evaluating the trade-offs between search costs, memory use, and maintenance work.

**Skills demonstrated:** data-structure selection, Trie implementation, hashing, set operations, sorting, complexity analysis, and correctness testing.

[Enhancement two narrative.docx](https://github.com/user-attachments/files/33940944/Enhancement.two.narrative.docx) | [View the milestone code](https://github.com/Insomnia-Arch/SNHU-capstone/commit/a3ccfa92b9300ee5bd79dc7895f88a057392a118)

### Enhancement Three: Databases

I added a MySQL repository through JDBC while retaining the existing service interface. Prepared statements, schema constraints, environment-based credentials, and database integration tests extended the application beyond temporary in-memory storage. The service rebuilds its search indexes from stored contacts when a new instance is created.

**Skills demonstrated:** relational database design, SQL, JDBC, persistent CRUD operations, secure query handling, resource management, and integration testing.

[Enhancement Three Narrative.docx](https://github.com/user-attachments/files/33940968/Enhancement.Three.Narrative.docx) | [View the final enhanced code](https://github.com/Insomnia-Arch/SNHU-capstone)

---

## Source Code and Downloads

| Material | Access |
| --- | --- |
| Original CS 320 artifact | [Original source](https://github.com/Insomnia-Arch/SNHU-capstone/commit/a91e39cbfe0fc786c5c4f23eec97066a06ea79ae) |
| Final enhanced application | [GitHub repository](https://github.com/Insomnia-Arch/SNHU-capstone) |
| Final test report | <img width="1067" height="586" alt="image" src="https://github.com/user-attachments/assets/8fa362b5-216f-4379-8db3-b1626b9aaada" />|
| Professional self-assessment | [Professional assement.docx](https://github.com/user-attachments/files/33941018/Professional.assement.docx) |

### Acknowledgments
This portfolio builds on my CS 320 coursework. Attribution for the original project's foundation is retained in the project documentation.

AI Use: During the development of this project, AI was almost impossible to avoid and I had to take steps intentionally to avoid it. I had to search for solutions to many of my coding problems such as how to structure the data, or for official Java documentation. When I would search specific problems in Google for research, the AI search feature that is most often at the top of the search results would quite often be the best way for me to find sources, so therefore technically AI was used to assist in the enhancement planning by helping me find sources and documentation, finding examples of coding use throughout online such as Stack Overflow, and during the code development phase, these suggestions helped me find some examples of code with similar design patterns. During the process of developing the code, VSCode offered autocompleted sections such as a setter/getter method, and sometimes I utilized this feature, and I do not know how much of that is from an AI.  I do not have a reference for these uses because they weren't substantial enough for me to cite, as these overviews did not make my decisions, and any decisions were based solely on the actual sources they had come from. 

##### No AI or LLM was responsible for the code that I wrote, its structure, my design decisions, or any of the enhancement narratives or deliverables for the Capstone.



### References for codebase research and planning

Parnas, D. L. (1972). On the criteria to be used in decomposing systems into modules. Communications of the ACM, 15(12), 1053–1058. https://doi.org/10.1145/361598.361623

Dr. Edward Lavieri Jr. (2024). High Performance with Java : Discover Strategies and Best Practices to Develop High Performance Java Applications. Packt Publishing.
GeeksforGeeks. (2026, September 22). Implement a phone directory. GeeksforGeeks

jitron. (2015, September 24). In Java, how can I map String to a Set if I do not know how many keys in total [Online forum post]. Stack Overflow.

Miroslav Wengner. (2023). Practical Design Patterns for Java Developers : Hone Your Software Design Skills by Implementing Popular Design Patterns in Java. Packt Publishing.

Mock data provided by [Mockaroo.com ](https://www.mockaroo.com/) to fill the MySQL Database for testing. 

[Back to top](#)
