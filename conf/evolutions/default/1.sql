# --- !Ups

create table Teachers (
  id bigserial unique primary key,
  name varchar(64) not null,
  created_at timestamp not null
);

create table Lectures (
  id bigserial unique primary key,
  name varchar(255) not null,
  teacher_id bigint not null,
  category varchar (64) not null,
  period int not null,
  remark text,
  graduate boolean not null,
  created_at timestamp not null
);

create table KyukoDays (
  id bigserial unique primary key,
  lecture_id bigint not null,
  date timestamp not null,
  created_at timestamp not null
);

# --- !Downs

drop table Teachers;
drop table Lectures;
drop table KyukoDays;
