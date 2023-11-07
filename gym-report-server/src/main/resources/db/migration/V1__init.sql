CREATE TABLE training_report (
    id SERIAL PRIMARY KEY,
    trainer_username VARCHAR(255) NOT NULL,
    trainer_firstname VARCHAR(255) NOT NULL,
    trainer_lastname VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    year INTEGER NOT NULL,
    month VARCHAR(255) NOT NULL,
    date INTEGER NOT NULL,
    training_duration INTEGER NOT NULL,
    type VARCHAR(255) NOT NULL
);