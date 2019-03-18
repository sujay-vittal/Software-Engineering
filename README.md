# UML Sequence Diagram Translator
## Functional Requirements:

1. Design Java classes that models the UML sequence diagram. Represent basic symbols and components of sequence diagram in Java based on the Sequence Diagram syntax standard.
	 - All the required standard symbols and components are in https://www.lucidchart.com/pages/uml-sequence-diagram

2. Generate Source code from the input file     (CSV Format)
	 - Use the tools designed by the Undergrad to parse the input file and generate AST Model. From AST model generate Java source code for the sequence diagram given by the user.

3. Generate Sequence diagram objects from Java source code
	 - Generate AST model from the input Java file(s) and create objects that represent the UML Sequence Diagram.

4. Compare UML Sequence Diagrams
	 - Compare UML Sequence Diagram objects and generate two outputs from the comparison:

		1. A boolean variable indicating whether the two diagrams compared are identical or not.

		2. If the two sets of diagrams do not match, then the non-matching objects from each set shall be identified and detailed differences are shown in the output.


## Non-Functional Requirements:

1. Error logs
	 - Specific exception (classes) shall be designed, implemented and documented (in Javadoc).
	 - All errors and exceptions shall be logged to a file. 
	 - In the delivered code no error or exception shall be printed to the console for performance reasons.

2. Speed
	 - The current speed requirement is that, for a sequence diagram containing 100 diagram objects or less, the conversion shall be completed within 2 seconds.(not hard requirement – negotiable)

3. Database layer 
	 - In the future the DB layer will be added to persist the Java objects, so considering this factor in the current design to facilitate future extension is preferred.

4. Coding Style
	 - https://google.github.io/styleguide/javaguide.html
5. Compatibility
	 - PC/Server platforms (i.e. Java SE). Hotspot JVM is the target JVM.
## UML Use Case Diagram
![UML-Use-Case-Diagram](umltranslator/doc/images/UML-use-case-diagram.png "UML-Use-Case-Diagram")

## UML Class Diagram
![UML-Class-Diagram](umltranslator/doc/images/UML-class-diagram.png "UML-Class-Diagram")
