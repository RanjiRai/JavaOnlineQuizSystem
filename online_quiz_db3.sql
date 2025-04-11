CREATE DATABASE IF NOT EXISTS online_quiz_db3;
USE online_quiz_db3;
drop database online_quiz_db3;
-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Quizzes Table
CREATE TABLE IF NOT EXISTS quizzes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Questions Table (Added explanation column)
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id INT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL CHECK (correct_option IN ('A', 'B', 'C', 'D')),
    explanation TEXT, -- New column for explanation
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
drop  table questions;
-- Scores Table
CREATE TABLE IF NOT EXISTS scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    quiz_id INT NOT NULL,
    score INT NOT NULL,
    time_taken INT DEFAULT 0,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    INDEX idx_user_quiz (user_id, quiz_id)
);
drop table scores;
-- Insert Sample Data
INSERT INTO quizzes (title) VALUES
('Java Basics Quiz'),
('Advanced Java Quiz'),
('OOP Concepts Quiz');

-- Insert 10 questions for Java Basics Quiz (Quiz ID 1) with explanations
INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation)
VALUES
(1, 'What is the default value of a boolean variable in Java?', 'true', 'false', 'null', '0', 'B', 'In Java, the default value of a boolean is false.'),
(1, 'Which keyword is used to define a constant in Java?', 'final', 'const', 'static', 'define', 'A', 'The "final" keyword makes a variable constant, meaning its value cannot be changed.'),
(1, 'Which of these loops is used when the number of iterations is known?', 'while', 'do-while', 'for', 'switch', 'C', 'The "for" loop is ideal when the number of iterations is predetermined.'),
(1, 'What is the size of an int variable in Java?', '2 bytes', '4 bytes', '8 bytes', 'Depends on the system', 'B', 'An int in Java is always 4 bytes (32 bits), regardless of the system.'),
(1, 'Which of the following is a valid variable name in Java?', '2variable', 'variable_name', 'variable-name', 'variable name', 'B', 'Variable names must start with a letter, underscore, or dollar sign and can include underscores.'),
(1, 'What is the output of System.out.println(5 + 3 + "Java");?', '8Java', '53Java', 'Java8', 'Java53', 'A', 'The numbers are added first (5 + 3 = 8), then concatenated with "Java".'),
(1, 'Which data type is used to store a single character in Java?', 'char', 'String', 'int', 'byte', 'A', 'The "char" type is used for single characters in Java.'),
(1, 'What is the correct way to declare a main method in Java?', 'public void main(String[] args)', 'public static void main(String[] args)', 'static void main(String args)', 'void main(String[] args)', 'B', 'The main method must be public, static, and take a String array as an argument.'),
(1, 'Which operator is used to compare two values in Java?', '=', '==', '!=', '===', 'B', 'The "==" operator compares primitive values or object references.'),
(1, 'What does JVM stand for?', 'Java Virtual Machine', 'Java Variable Manager', 'Java Version Model', 'Java Visual Machine', 'A', 'JVM is the Java Virtual Machine, which executes Java bytecode.');

INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation)
VALUES
(2, 'What is the purpose of the transient keyword in Java?', 'It makes a variable static', 'It allows serialization', 'It prevents serialization', 'It defines a constant', 'C', 'The "transient" keyword prevents a variable from being serialized.'),
(2, 'Which collection class allows duplicate elements and maintains insertion order?', 'HashSet', 'TreeSet', 'LinkedHashSet', 'ArrayList', 'D', 'ArrayList allows duplicates and maintains insertion order.'),
(2, 'Which interface does java.util.HashMap implement?', 'Map', 'List', 'Set', 'Collection', 'A', 'HashMap implements the Map interface, allowing key-value pairs.'),
(2, 'What does the "volatile" keyword indicate in Java?', 'Variable cannot be changed', 'Variable is constant', 'Variable is visible to all threads', 'Variable is static', 'C', 'Volatile ensures that the variable is read from main memory, providing visibility across threads.'),
(2, 'Which method is called when an object is garbage collected?', 'destroy()', 'finalize()', 'dispose()', 'remove()', 'B', 'The "finalize()" method is invoked before garbage collection to allow cleanup.'),
(2, 'What is the result of priority inversion?', 'Faster execution', 'Deadlock', 'Starvation of high-priority thread', 'Thread synchronization', 'C', 'Priority inversion occurs when a low-priority thread holds a resource needed by a high-priority thread.'),
(2, 'Which class is used for creating immutable objects in Java?', 'String', 'StringBuilder', 'StringBuffer', 'CharSequence', 'A', 'String is immutable, meaning once created, it cannot be changed.'),
(2, 'Which of the following supports thread-safe, synchronized collection?', 'ArrayList', 'Vector', 'LinkedList', 'HashSet', 'B', 'Vector is synchronized and thus thread-safe.'),
(2, 'What is a marker interface in Java?', 'An interface with methods', 'An interface with constants', 'An interface with fields', 'An interface with no methods or fields', 'D', 'Marker interfaces (e.g., Serializable) are empty and used to convey metadata.'),
(2, 'Which of the following exceptions is unchecked?', 'IOException', 'SQLException', 'ArrayIndexOutOfBoundsException', 'FileNotFoundException', 'C', 'ArrayIndexOutOfBoundsException is a runtime exception and does not need to be declared.');

INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation)
VALUES
(3, 'What is encapsulation in Java?', 'Hiding implementation details', 'Deriving classes', 'Overloading methods', 'Using interfaces', 'A', 'Encapsulation is the practice of hiding internal details and exposing functionality via public methods.'),
(3, 'Which concept allows different classes to be treated as one type?', 'Encapsulation', 'Abstraction', 'Polymorphism', 'Inheritance', 'C', 'Polymorphism allows one interface to be used for a general class of actions.'),
(3, 'Which keyword is used to inherit a class in Java?', 'implement', 'extends', 'inherits', 'instanceof', 'B', 'The "extends" keyword is used for class inheritance in Java.'),
(3, 'What is method overloading?', 'Changing the return type', 'Using the same method name with different parameters', 'Overriding superclass methods', 'Hiding methods', 'B', 'Overloading means defining multiple methods with the same name but different parameters.'),
(3, 'What is the benefit of abstraction?', 'Performance optimization', 'Better memory usage', 'Hiding complex implementation', 'Increased coupling', 'C', 'Abstraction helps hide complexity and show only relevant details.'),
(3, 'Which principle of OOP is achieved using interfaces?', 'Inheritance', 'Encapsulation', 'Polymorphism', 'Abstraction', 'D', 'Interfaces define a contract and achieve abstraction.'),
(3, 'Which of the following is not a feature of OOP?', 'Encapsulation', 'Abstraction', 'Compilation', 'Polymorphism', 'C', 'Compilation is a process, not an OOP feature.'),
(3, 'What is the purpose of the "super" keyword in Java?', 'To define a superclass', 'To access parent class members', 'To inherit a class', 'To define interfaces', 'B', 'The "super" keyword is used to refer to the parent class constructors and methods.'),
(3, 'What does dynamic method dispatch mean?', 'Method calling at compile time', 'Method binding during runtime', 'Method overloading', 'Method hiding', 'B', 'Dynamic method dispatch is the process where method call is resolved at runtime.'),
(3, 'Which OOP concept is most closely associated with reuse?', 'Encapsulation', 'Inheritance', 'Polymorphism', 'Abstraction', 'B', 'Inheritance promotes code reuse by allowing child classes to inherit parent class members.');

-- Insert Sample Users
INSERT INTO users (username, password, role) VALUES
('administrator', 'admin@3927', 'ADMIN'),
('ram', 'ram123', 'USER');

-- Insert Sample Scores
INSERT INTO scores (user_id, quiz_id, score, time_taken) VALUES
(2, 1, 80, 120),
(2, 2, 65, 150),
(2, 3, 90, 100);

select * from questions;
select * from users;

