CREATE TABLE servers (
    id SERIAL PRIMARY KEY,
    reference VARCHAR NOT NULL,
    schedule_id INT NOT NULL,

    CONSTRAINT fk_schedule
    FOREIGN KEY(schedule_id)
    REFERENCES schedules(id)
);