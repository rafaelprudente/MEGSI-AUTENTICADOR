CREATE TABLE IF NOT EXISTS `tb_users`
(
    `id`                      uuid         NOT NULL,
    `username`                varchar(200) NOT NULL,
    `password`                varchar(129) DEFAULT NULL,
    `email`                   varchar(200) NOT NULL,
    `name`                    varchar(120) NOT NULL,
    `change_password_hash`    varchar(255) DEFAULT NULL,
    `enabled`                 tinyint(1)   NOT NULL,
    `request_change_password` tinyint(1)   DEFAULT NULL,
    `account_non_expired`     tinyint(1)   DEFAULT NULL,
    `account_non_locked`      tinyint(1)   DEFAULT NULL,
    `credentials_non_expired` tinyint(1)   NOT NULL,
    `created_at`              datetime     NOT NULL,
    `updated_at`              datetime     NOT NULL,
    `expired_at`              datetime     DEFAULT NULL,
    `locked_at`               datetime     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS tb_roles
(
    `id`        BIGINT PRIMARY KEY AUTO_INCREMENT,
    `authority` VARCHAR(255) NOT NULL,
    UNIQUE KEY `authority` (`authority`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS tb_user_roles
(
    `user_id` UUID   NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `FKhjusdfgsdfgsdfglig05ktofg` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`id`),
    CONSTRAINT `FKhjuy9y4fd8v5m3klig05ktofg` FOREIGN KEY (`role_id`) REFERENCES `tb_roles` (`id`)
);

INSERT IGNORE INTO tb_roles (id, authority)
VALUES (1, 'ROLE_SYSTEM_ADMIN');
INSERT IGNORE INTO tb_roles (id, authority)
VALUES (2, 'ROLE_ADMIN');
INSERT IGNORE INTO tb_roles (id, authority)
VALUES (3, 'ROLE_FISCAL');
INSERT IGNORE INTO tb_roles (id, authority)
VALUES (4, 'ROLE_DRIVER');

SET @ADMIN_ID = UUID();

INSERT IGNORE INTO tb_users (id, username, password, email, name, change_password_hash, enabled,
                             request_change_password, account_non_expired, account_non_locked, credentials_non_expired,
                             created_at, updated_at, expired_at, locked_at)
VALUES (@ADMIN_ID,
        'admin',
        '$2a$12$GyySjWQzHBLovPDlE6Gcn.X58TmTqrTxWP3aBjLTpE/nwZlIFX0Fy',
        'rafael.prudente.santos@gmail.com',
        'Rafael Santos',
        null,
        true,
        true,
        true,
        true,
        true,
        NOW(),
        NOW(),
        NULL,
        NULL);

INSERT IGNORE INTO tb_user_roles (user_id, role_id)
VALUES (@ADMIN_ID, 1);