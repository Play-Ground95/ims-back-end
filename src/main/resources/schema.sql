CREATE TABLE IF NOT EXISTS teacher(
                                      id INT AUTO_INCREMENT PRIMARY KEY ,
                                      name VARCHAR(100) NOT NULL ,
                                      contact VARCHAR(100) NOT NULL

);

CREATE TABLE IF NOT EXISTS course(
                                     id INT AUTO_INCREMENT PRIMARY KEY ,
                                     name VARCHAR(50) NOT NULL ,
                                     duration_in_moth INT NOT NULL
);