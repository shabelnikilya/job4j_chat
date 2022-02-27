CREATE TABLE IF NOT EXISTS persons(
    id SERIAL PRIMARY KEY,
    name VARCHAR(30),
    surname VARCHAR(30),
    login VARCHAR(200) UNIQUE,
    password VARCHAR(200),
    role VARCHAR(100),
    registration TIMESTAMP
);
CREATE TABLE IF NOT EXISTS rooms(
    id SERIAL PRIMARY KEY,
    name VARCHAR(30),
    password VARCHAR(200)
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