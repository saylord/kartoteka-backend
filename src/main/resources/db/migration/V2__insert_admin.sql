insert into users (id, firstname, lastname, phone, active, role)
values (
           nextVal('users_id_seq'),
           'Admin',
           'Test',
           '+7-7777777777',
           true,
           'ADMIN'
       );

insert into auth (id, enabled, password, user_id, username)
values (
           nextVal('auth_id_seq'),
           true,
           '$2a$10$OPPhZJ1ZU0UrJxknabaL4eHatVDyueAP54cet4.7yM9pG0riARFlu',
           1,
           'admin@gmail.com'
       );

INSERT INTO secret (name)
VALUES ('secret');

INSERT INTO building (name)
VALUES ('building');

INSERT INTO ingoing (document_number, description, resolution, status, created_timestamp, document_timestamp, building_id, secret_id, copy_number, copy_sheet, sheet, schedule)
VALUES ('1234', 'description', 'qqq', 'OPENED', 1710775661623, 1710775661623, 1, 1, 1, 2, 3, 4);
