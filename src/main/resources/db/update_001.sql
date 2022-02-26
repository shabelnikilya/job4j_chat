CREATE TABLE IF NOT EXISTS persons(
    id SERIAL PRIMARY KEY,
    name VARCHAR(30),
    surname VARCHAR(30),
    login VARCHAR(10) UNIQUE,
    password VARCHAR(10),
    role VARCHAR(10),
    registration TIMESTAMP
);
CREATE TABLE IF NOT EXISTS rooms(
    id SERIAL PRIMARY KEY,
    name VARCHAR(30),
    password VARCHAR(10)
);
CREATE TABLE IF NOT EXISTS messages(
    id SERIAL PRIMARY KEY,
    text VARCHAR(1000),
    created TIMESTAMP,
    persons_id INT NOT NULL REFERENCES persons(id),
    rooms_id INT NOT NULL REFERENCES rooms(id)
);
CREATE TABLE IF NOT EXISTS rooms_persons(
    id SERIAL PRIMARY KEY,
    rooms_id INT NOT NULL REFERENCES rooms(id),
    persons_id INT NOT NULL REFERENCES persons(id)
);