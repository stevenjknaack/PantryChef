# populates PantryChef database for Windows

SET GLOBAL local_infile=true;

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Recipe.csv" INTO TABLE Recipe FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Image.csv" INTO TABLE Image FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Illustrates.csv" INTO TABLE Illustrates FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/CallsFor.csv" INTO TABLE CallsFor FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/User.csv" INTO TABLE User FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

# for hashing passwords
UPDATE User SET Password = UNHEX(SHA(Password));

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/HasIngredient.csv" INTO TABLE HasIngredient FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Favorites.csv" INTO TABLE Favorites FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";

LOAD DATA INFILE "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Review.csv" INTO TABLE Review FIELDS TERMINATED BY "," ENCLOSED BY '"' LINES TERMINATED BY "\r\n";