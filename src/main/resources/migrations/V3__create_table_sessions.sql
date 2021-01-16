CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    teacher VARCHAR NOT NULL,
    location VARCHAR NOT NULL,
    date_session DATE NOT NULL,
    start_time TIME NOT NUll,
    end_time TIME NOT NULL,
    schedule_id INT NOT NULL,

    CONSTRAINT fk_schedule
    FOREIGN KEY(schedule_id)
    REFERENCES schedules(id)
);
