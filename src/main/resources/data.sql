INSERT IGNORE INTO rhs_tomapp.department
(id, name)
VALUES('mae4bc56-d877-4d93-8a97-f3aec65aa7e7', 'IT');


INSERT IGNORE INTO rhs_tomapp.employee
(id, address, email, emp_date, name, salary, tel, fk_department)
VALUES('ff352e3e-4f6b-4aac-9e2a-f4a0a4249b6c', 'Str. MDJ 10', 'rosualinpetru@gmail.com', '2021-12-12 22:00:00', 'Rosu Alin-Petru', 6000, '0756565656', 'mae4bc56-d877-4d93-8a97-f3aec65aa7e7');

INSERT IGNORE INTO rhs_tomapp.account
(id, activated, password, remaining_days, salt, username, fk_employee, fk_team_leader)
VALUES('bae4bc56-d877-4d93-8a97-f3aec65aa7e7', 1, '$2a$10$7vCj5T2LLfTkRj01A.e1BeLOmM7WYZSPzyYMwIEDRHJQLLdNikam2', 30, 'abcdef', 'arosu', 'ff352e3e-4f6b-4aac-9e2a-f4a0a4249b6c', NULL);
