# setup for PantryChef database in mySQL

CREATE DATABASE PantryChef;

USE PantryChef;

CREATE TABLE Recipe (
  RecipeID INTEGER,
  Name VARCHAR(300),
  Instructions VARCHAR(11000),
  TimeDescription VARCHAR(100),
  Servings VARCHAR(100),
  NutrionalContent VARCHAR(300),
  Description VARCHAR(3500),
  AuthorUsername VARCHAR(50),
  DatePublished VARCHAR(30),
  DateModified VARCHAR(30),
  PRIMARY KEY (RecipeID)
);

CREATE INDEX RecipeIDIndex
ON Recipe (RecipeID);

CREATE INDEX RecipeAuthorIndex
ON Recipe (AuthorUsername);

CREATE TABLE Image (
  ImageID INTEGER,
  Link VARCHAR(2000),
  PRIMARY KEY (ImageID)
);

CREATE TABLE Illustrates (
  ImageID INTEGER,
  RecipeID INTEGER,
  PRIMARY KEY (ImageID, RecipeID)
);

CREATE TABLE CallsFor (
  RecipeID INTEGER,
  IngredientName VARCHAR(100),
  Quantity VARCHAR(10),
  PRIMARY KEY (RecipeID, IngredientName)
);

CREATE INDEX IngredientIndex
ON CallsFor (IngredientName);

CREATE TABLE User (
  Username VARCHAR(50),
  Password VARCHAR(50),
  PRIMARY KEY (Username)
);

CREATE TABLE HasIngredient (
  IngredientName VARCHAR(100),
  Username VARCHAR(50),
  PRIMARY KEY (IngredientName, Username)
);

CREATE TABLE Favorites (
  RecipeID INTEGER,
  Username VARCHAR(50),
  PRIMARY KEY (RecipeID, Username)
);

CREATE TABLE Review (
  ReviewID INTEGER,
  Review VARCHAR(10000),
  Rating INTEGER,
  RecipeID INTEGER,
  AuthorUsername VARCHAR(50),
  DateSubmitted VARCHAR(30),
  DateModified VARCHAR(30),
  PRIMARY KEY (ReviewID)
);

CREATE INDEX ReviewIndex
ON Review (RecipeID);