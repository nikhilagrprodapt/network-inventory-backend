CREATE DATABASE IF NOT EXISTS network_inventory_db;
USE network_inventory_db;

CREATE TABLE headend (
  headend_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  location VARCHAR(255),
  bandwidth_capacity_mbps INT
);

CREATE TABLE fdh (
  fdh_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  location VARCHAR(255),
  region VARCHAR(255),
  max_ports INT,
  headend_id BIGINT,
  CONSTRAINT fk_fdh_headend FOREIGN KEY (headend_id) REFERENCES headend(headend_id)
);

CREATE TABLE splitter (
  splitter_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  model VARCHAR(255),
  port_capacity INT,
  fdh_id BIGINT,
  CONSTRAINT fk_splitter_fdh FOREIGN KEY (fdh_id) REFERENCES fdh(fdh_id)
);

CREATE TABLE customer (
  customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  neighborhood VARCHAR(255),
  plan VARCHAR(255),
  connection_type VARCHAR(20),
  status ENUM('PENDING','ACTIVE','INACTIVE') NOT NULL,
  splitter_id BIGINT,
  splitter_port INT,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT fk_customer_splitter FOREIGN KEY (splitter_id) REFERENCES splitter(splitter_id),
  UNIQUE KEY uq_splitter_port (splitter_id, splitter_port)
);

CREATE TABLE technician (
  technician_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  contact VARCHAR(255),
  region VARCHAR(255)
);

CREATE TABLE deployment_task (
  task_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id BIGINT,
  technician_id BIGINT,
  task_type VARCHAR(100),
  status VARCHAR(50),
  scheduled_at DATETIME,
  started_at DATETIME,
  completed_at DATETIME,
  notes VARCHAR(2000),
  CONSTRAINT fk_task_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
  CONSTRAINT fk_task_technician FOREIGN KEY (technician_id) REFERENCES technician(technician_id)
);

CREATE TABLE asset (
  asset_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type VARCHAR(100),
  model VARCHAR(255),
  serial_number VARCHAR(255),
  status VARCHAR(50),
  assigned_to_customer_id BIGINT,
  assigned_at DATETIME,
  CONSTRAINT fk_asset_customer FOREIGN KEY (assigned_to_customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE audit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  actor VARCHAR(255),
  action VARCHAR(100),
  entity_type VARCHAR(100),
  entity_id BIGINT,
  details VARCHAR(2000),
  request_id VARCHAR(255),
  created_at DATETIME
);
