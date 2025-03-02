DROP TABLE IF EXISTS users;

CREATE TABLE `users` (
  `user_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_age` tinyint(4) NOT NULL,
  `user_gender` varchar(100) NOT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_profile` varchar(200) NULL,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_date` timestamp NULL,
  PRIMARY KEY (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS chats;

CREATE TABLE `chats` (
  `chat_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_id` varchar(20) NOT NULL,
  `to_id` varchar(20) NOT NULL,
  `chat_content`  varchar(200) NOT NULL ,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`chat_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS boards;

CREATE TABLE `boards` (
  `board_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20),
  `board_title` varchar(50),
  `board_content` varchar(2048),
  `board_hit` int(4) DEFAULT 0,
  `board_file` varchar(100),
  `board_real_file` varchar(100),
  `board_group` int(4) DEFAULT 0,
  `board_sequence` int(4) DEFAULT 0,
  `board_level` int(4) DEFAULT 0,
  `board_available` int(4) DEFAULT 1,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_date` timestamp NULL,
  PRIMARY KEY (`board_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


