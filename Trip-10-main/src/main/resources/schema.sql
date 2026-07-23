CREATE TABLE IF NOT EXISTS admin_permission (
    id INT NOT NULL AUTO_INCREMENT,
    can_verify_documents BOOLEAN,
    can_reject_documents BOOLEAN,
    can_approve_drivers BOOLEAN,
    can_manage_admins BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS permission (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS admin (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    permission_id INT,
    is_super_admin BOOLEAN,
    PRIMARY KEY (id),
    CONSTRAINT fk_admin_permission_id
        FOREIGN KEY (permission_id) REFERENCES admin_permission (id)
);

CREATE TABLE IF NOT EXISTS user (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    permission_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_permission_id
        FOREIGN KEY (permission_id) REFERENCES admin_permission (id)
);

CREATE TABLE IF NOT EXISTS customer (
    id INT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    otp_verified BOOLEAN DEFAULT FALSE,
    otp_code VARCHAR(255),
    otp_expires_at DATETIME,
    created_at DATETIME,
    deleted_at DATETIME,
    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (phone_number)
);

CREATE TABLE IF NOT EXISTS document (
    id INT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    uploaded_at DATETIME,
    auth_status VARCHAR(30) DEFAULT 'PENDING',
    document_type VARCHAR(30),
    owner_type VARCHAR(30),
    updated_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS driver (
    id INT NOT NULL AUTO_INCREMENT,
    driver_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    auth_status VARCHAR(30) DEFAULT 'PENDING',
    otp_verification BOOLEAN,
    otp_code VARCHAR(255),
    otp_expires_at DATETIME,
    created_at DATETIME,
    deleted_at DATETIME,
    driver_type VARCHAR(30),
    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (phone_number)
);

CREATE TABLE IF NOT EXISTS car (
    id INT NOT NULL AUTO_INCREMENT,
    doc_id INT,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    plate_number VARCHAR(255) NOT NULL,
    created_at DATETIME,
    verification_status VARCHAR(30),
    PRIMARY KEY (id),
    UNIQUE (plate_number)
);

CREATE TABLE IF NOT EXISTS car_doc (
    id INT NOT NULL AUTO_INCREMENT,
    document_id INT,
    car_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    uploaded_at DATETIME,
    auth_status VARCHAR(30) DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS driver_doc (
    id INT NOT NULL AUTO_INCREMENT,
    driver_id INT NOT NULL,
    document_id INT,
    file_name VARCHAR(255) NOT NULL,
    uploaded_at DATETIME,
    path VARCHAR(255),
    auth_status VARCHAR(30) DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS trip (
    id INT NOT NULL AUTO_INCREMENT,
    driver_id INT,
    customer_id INT,
    start_location VARCHAR(255),
    end_location VARCHAR(255),
    distance DOUBLE,
    price DOUBLE,
    start_time DATETIME,
    end_time DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME,
    trip_status VARCHAR(30),
    trip_type VARCHAR(30),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS api_log (
    id INT NOT NULL AUTO_INCREMENT,
    http_method VARCHAR(10),
    endpoint VARCHAR(255),
    request_body TEXT,
    response_body TEXT,
    status_code INT,
    performed_by VARCHAR(255),
    ip_address VARCHAR(45),
    created_at DATETIME,
    PRIMARY KEY (id)
);
