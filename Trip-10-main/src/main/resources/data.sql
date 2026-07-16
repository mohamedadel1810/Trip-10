INSERT IGNORE INTO admin_permission (id, can_verify_documents, can_reject_documents, can_approve_drivers, can_manage_admins)
VALUES
    (1, TRUE, TRUE, TRUE, TRUE),
    (2, FALSE, FALSE, FALSE, FALSE);

INSERT IGNORE INTO permission (id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'DRIVER'),
    (3, 'CUSTOMER');

-- password for every seeded account below is: Password123!
INSERT IGNORE INTO admin (id, name, email, password, permission_id, is_super_admin)
VALUES
    (1, 'Super Admin', 'admin@trip10.com', '$2b$10$qu6lK5l3ALED9vUOqHW0UubEWUUT9uVlz.RfDEiHhG7OPALxxaZIm', 1, TRUE),
    (2, 'Support Admin', 'support@trip10.com', '$2b$10$qu6lK5l3ALED9vUOqHW0UubEWUUT9uVlz.RfDEiHhG7OPALxxaZIm', 2, FALSE);

INSERT IGNORE INTO user (id, username, permission_id)
VALUES
    (1, 'seed_user', 1);

INSERT IGNORE INTO customer (id, user_name, email, password, phone_number, otp_verified, created_at)
VALUES
    (1, 'Sara Ali', 'sara.ali@example.com', '$2b$10$qu6lK5l3ALED9vUOqHW0UubEWUUT9uVlz.RfDEiHhG7OPALxxaZIm', '01011111111', FALSE, NOW()),
    (2, 'Mona Youssef', 'mona.youssef@example.com', '$2b$10$qu6lK5l3ALED9vUOqHW0UubEWUUT9uVlz.RfDEiHhG7OPALxxaZIm', '01022222222', TRUE, NOW());

INSERT IGNORE INTO document (id, file_name, path, uploaded_at, auth_status, document_type, owner_type)
VALUES
    (1, 'national_id_front.jpg', '/uploads/docs/national_id_front.jpg', NOW(), 'PENDING', 'PERSONAL_DOC', 'DRIVER'),
    (2, 'car_license.jpg', '/uploads/docs/car_license.jpg', NOW(), 'PENDING', 'CAR_DOC', 'CAR');

INSERT IGNORE INTO driver (id, driver_name, email, password, phone_number, auth_status, otp_verification, created_at, driver_type)
VALUES
    (1, 'Ahmed Mostafa', 'ahmed.driver@example.com', '$2b$10$qu6lK5l3ALED9vUOqHW0UubEWUUT9uVlz.RfDEiHhG7OPALxxaZIm', '01012345678', 'PENDING', FALSE, NOW(), 'INDIVIDUAL_DRIVER'),
    (2, 'Karim Hassan', 'karim.driver@example.com', '$2b$10$qu6lK5l3ALED9vUOqHW0UubEWUUT9uVlz.RfDEiHhG7OPALxxaZIm', '01098765432', 'VERIFIED', TRUE, NOW(), 'COMPANY_DRIVER');

INSERT IGNORE INTO car (id, doc_id, brand, model, color, plate_number, created_at, verification_status)
VALUES
    (1, 0, 'Toyota', 'Corolla', 'White', 'ABC1234', NOW(), 'PENDING');

INSERT IGNORE INTO car_doc (id, document_id, car_id, file_name, path, uploaded_at, auth_status)
VALUES
    (1, 2, 1, 'car_license.jpg', '/uploads/docs/car_license.jpg', NOW(), 'PENDING');

INSERT IGNORE INTO driver_doc (id, driver_id, document_id, file_name, uploaded_at, path, auth_status)
VALUES
    (1, 1, 1, 'national_id_front.jpg', NOW(), '/uploads/docs/national_id_front.jpg', 'PENDING');

INSERT IGNORE INTO otp_verification (id, phone_number, otp_code, expires_at, is_verified)
VALUES
    (1, '01012345678', '123456', DATE_ADD(NOW(), INTERVAL 5 MINUTE), FALSE);
