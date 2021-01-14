CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    teacher VARCHAR NOT NULL,
    location VARCHAR NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NUll,
    schedule_id INT NOT NULL,

    CONSTRAINT fk_schedule
    FOREIGN KEY(schedule_id)
    REFERENCES schedules(id)
);
