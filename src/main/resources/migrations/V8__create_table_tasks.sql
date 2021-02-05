CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    server_id INT NOT NULL,
	description VARCHAR NOT NULL,
	due_date DATE NOT NULL,
    due_time TIME NOT NULL,

	CONSTRAINT fk_server
    FOREIGN KEY(server_id)
    REFERENCES servers(id)
);
