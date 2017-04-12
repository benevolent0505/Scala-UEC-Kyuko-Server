# --- !Ups
alter table KyukoDays add remark text;
alter table Lectures drop remark;

# --- !Downs
