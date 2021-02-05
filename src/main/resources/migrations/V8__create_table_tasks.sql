CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    server_id INT NOT NULL,
	description VARCHAR NOT NULL,
	due_date DATE,
    due_time TIME,

	CONSTRAINT fk_server
    FOREIGN KEY(server_id)
    REFERENCES servers(id)
);
