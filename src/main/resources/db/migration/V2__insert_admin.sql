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

INSERT INTO ingoing (document_number, card_number, description, resolution, status, created_timestamp, estimated_timestamp, document_timestamp, building_id, secret_id, total_sheet, sheet, schedule, annual, semi_annual, monthly, ten_day, annual_timestamp, semi_annual_timestamp, monthly_timestamp, ten_day_timestamp)
VALUES ('document number', '1 secret', 'description', 'qqq', 'OPENED', 1710775661623, 1743348461000, 1710775661623, 1, 1, 5, 4, 1, true, false, false, false, 1742311661000, 1726673261000, 1713454061000, 1711639661000);

insert into users (id, firstname, lastname, phone, active, role)
values (
           nextVal('users_id_seq'),
           'Executor',
           'Test',
           '+7-7777777777',
           true,
           'EXECUTOR'
);

INSERT INTO outgoing (document_number, card_number, description, exemplar, status, created_timestamp, estimated_timestamp, document_timestamp, building_id, secret_id, total_sheet, sheet, schedule, executor_id, doc_department_index, doc_copy_print, ingoing_id, interim_timestamp)
VALUES ('12345doc', '1 secret', 'description', 'qqq', 'OPENED', 1710775661623, 1710782866000, 1710775661623, 1, 1, 50, 49, 1, 2, 'индекс 001', 2, 1, 1710782866000);
