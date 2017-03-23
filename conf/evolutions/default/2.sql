# --- !Ups
alter table KyukoDays add remark text;

# --- !Downs
alter table Lectures drop remark;
