-- 事件归属应用迁移脚本
-- 执行前请确认 tracking_event 当前仍使用 uk_event_code 唯一索引。

ALTER TABLE tracking_event
    ADD COLUMN app_code VARCHAR(128) NULL COMMENT '应用编码' AFTER id;

UPDATE tracking_event
SET app_code = 'dataman_web'
WHERE app_code IS NULL OR app_code = '';

UPDATE tracking_event
SET event_code = 'platform_module_click',
    business_domain = 'platform',
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1000001 OR event_code = 'module_click';

INSERT INTO tracking_app (id, app_code, app_name, app_type, description, secret_key, report_url, status, created_by, updated_by)
VALUES (1000001, 'dataman_web', '数据平台 Web', 'WEB', 'cyan-dataman-web 前端自身埋点应用', 'system', '/rpc/data-collection/collect/events', 'ENABLED', 'system', 'system')
ON DUPLICATE KEY UPDATE app_name = VALUES(app_name), app_type = VALUES(app_type), report_url = VALUES(report_url), status = VALUES(status), updated_at = CURRENT_TIMESTAMP;

ALTER TABLE tracking_event
    MODIFY COLUMN app_code VARCHAR(128) NOT NULL COMMENT '应用编码';

ALTER TABLE tracking_event
    DROP INDEX uk_event_code;

ALTER TABLE tracking_event
    ADD UNIQUE KEY uk_app_event_code (app_code, event_code);

ALTER TABLE tracking_event
    ADD INDEX idx_app_code (app_code);
