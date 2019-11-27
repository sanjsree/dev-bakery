# dev-bakery
Backend bakery solution app

This is an implementation for a bakery app.

It basically takes in orders for different products and gives total order cost with breakup for each product.

As of now, the application receives orders for only 3 products:

Vegemite Scroll(VS5) Blueberry Muffin(MB11) Croissant(CF)

To save on shipping space each order should contain the minimal number of packs.

Sample Input:
Each order has a series of lines with each line containing the number of items followed by the product code alternatively. An example input: 10 VS5 14 MB11 13 CF

Sample Output:
A successfully passing test(s) that demonstrates the following output:

10 VS5 $17.98 2 x 5 $8.99 14 MB11 $54.8 1 x 8 $24.95 3 x 2 $9.95 13 CF $25.85 2 x 5 $9.95 1 x 3 $5.95

This project is implemented using Java 11, JUnit and Maven.

Steps for build and execution
-----------------------------
a) Inside the project foilder, where pom.xml file is present, run this command-> mvn clean install.
b) Upon successful build, A jar file will be generated (app-0.0.1-SNAPSHOT.jar). Please copy the same to any folder on the target linux system.
c) after that, go to the folder and run the below command:
  java -jar app-0.0.1-SNAPSHOT.jar
d) Input will be taken from the command line / console and is not hardcoded.
