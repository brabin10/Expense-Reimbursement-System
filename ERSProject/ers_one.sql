
/**************************************************************************************************************
                                    CREATE THE TABLES FOR THE DATABASE
**************************************************************************************************************/

create table ers_reimbursement (
  reimb_id serial primary key,
  reimb_amount int not null,
  reimb_submitted timestamp not null,
  reimb_resolved timestamp,
  reimb_description varchar(250),
  reimb_author int not null,
  reimb_resolver int,
  reimb_status_id int not null,
  reimb_type_id int not NULL,
  
  FOREIGN KEY (reimb_author) REFERENCES ers_users (ers_users_id),
  FOREIGN KEY (reimb_resolver) REFERENCES ers_users (ers_users_id),
  FOREIGN KEY (reimb_status_id) REFERENCES ers_reimbursement_status (reimb_status_id),
  FOREIGN KEY (reimb_type_id) REFERENCES ers_reimbursement_type (reimb_type_id)
);

create table ers_users (
    ers_users_id serial primary key,
    ers_username varchar(50) not null unique,
    ers_password varchar(50) not null,
    user_first_name varchar(100) not null,
    user_last_name varchar(100) not null,
    user_email varchar(150) not null unique,
    user_role_id int not NULL,
    
    FOREIGN KEY (user_role_id) REFERENCES ers_user_roles (ers_user_role_id)
);

create table ers_reimbursement_status (
    reimb_status_id serial primary key,
    reimb_status varchar(10) not null
);

create table ers_reimbursement_type (
    reimb_type_id serial primary key,
    reimb_type varchar(10) not null
);

create table ers_user_roles (
    ers_user_role_id serial primary key,
    user_role varchar(10) not null
);


/*********************************************************************************************
                            INITIALIZE DATABASE VALUES
**********************************************************************************************/

/* reimbursement status codes */
INSERT INTO ers_reimbursement_status (reimb_status_id, reimb_status)
VALUES (1, 'Pending');
INSERT INTO ers_reimbursement_status (reimb_status_id, reimb_status)
VALUES (2, 'Approved');
INSERT INTO ers_reimbursement_status (reimb_status_id, reimb_status)
VALUES (3, 'Denied');

/* reimbursement type codes */
INSERT INTO ers_reimbursement_type (reimb_type_id, reimb_type)
VALUES (1, 'Lodging');
INSERT INTO ers_reimbursement_type (reimb_type_id, reimb_type)
VALUES (2, 'Travel');
INSERT INTO ers_reimbursement_type (reimb_type_id, reimb_type)
VALUES (3, 'Food');
INSERT INTO ers_reimbursement_type (reimb_type_id, reimb_type)
VALUES (4, 'Other');

/* ers user roles */
INSERT INTO ers_user_roles (ers_user_role_id, user_role)
VALUES (1, 'Employee');
INSERT INTO ers_user_roles (ers_user_role_id, user_role)
VALUES (2, 'Manager');

/* insert primary user */
INSERT INTO ers_users (ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id)
VALUES (1, 'rbasnet', 'password', 'Rabin', 'Basnet', 'rbasnet@email.com', 2);

    
SELECT * FROM ers_users;
SELECT * FROM ers_user_roles;
SELECT * FROM ers_reimbursement_type;
SELECT * FROM ers_reimbursement_status;
