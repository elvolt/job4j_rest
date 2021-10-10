create table employee
(
    id      serial primary key not null,
    name    varchar(2000),
    surname varchar(2000),
    inn     varchar(12),
    hiring  date
);

create table person
(
    id          serial primary key not null,
    login       varchar(2000),
    password    varchar(2000),
    employee_id int references employee (id)
);
insert into employee (name, surname, inn, hiring)
values ('name 1', 'surname 1', '1234567', '2020-10-10');
insert into employee (name, surname, inn, hiring)
values ('name 2', 'surname 2', '1234567', '2020-03-11');
insert into person (login, password, employee_id)
values ('parsentev', '123', 1);
insert into person (login, password, employee_id)
values ('ban', '123', 2);
insert into person (login, password, employee_id)
values ('ivan', '123', 1);