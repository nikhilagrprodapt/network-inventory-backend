USE network_inventory_db;

INSERT INTO headend (name, location, bandwidth_capacity_mbps)
VALUES ('Headend-1', 'HQ', 10000);

INSERT INTO fdh (name, location, region, max_ports, headend_id)
VALUES ('FDH-1', 'Zone-A', 'A', 64, 1);

INSERT INTO splitter (name, model, port_capacity, fdh_id)
VALUES ('Splitter-1', 'SPL-8', 8, 1);

INSERT INTO technician (name, contact, region)
VALUES ('Tech-1', '9999999999', 'A');

INSERT INTO asset (type, model, serial_number, status)
VALUES ('ONT', 'ONT-X', 'ONT-12345', 'AVAILABLE');
