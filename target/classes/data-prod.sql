-- Initial admin account (you should change this password after first login)
INSERT INTO users (username, password, role, email, full_name) 
VALUES ('admin', '$2a$10$fixedSaltForDevEnvironment.XJ6QFr2EwNTh3V0H0G/ku/WLkQQ2', 'ADMIN', 'admin@yourdomain.com', 'System Administrator')
ON DUPLICATE KEY UPDATE username=username; 