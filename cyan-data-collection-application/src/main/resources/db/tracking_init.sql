-- 数据采集中心（埋点系统）MVP 核心表初始化
-- 数据库: cyan_data_collection

-- 事件定义表
CREATE TABLE IF NOT EXISTS tracking_event (
    id BIGINT NOT NULL PRIMARY KEY,
    event_code VARCHAR(128) NOT NULL COMMENT '事件编码',
    event_name VARCHAR(255) NOT NULL COMMENT '事件名称',
    event_type VARCHAR(64) NOT NULL COMMENT '事件类型: PAGE_VIEW, CLICK, SUBMIT, SEARCH, TRANSACTION, SYSTEM, CUSTOM',
    business_domain VARCHAR(128) COMMENT '业务域',
    description TEXT COMMENT '业务描述',
    trigger_timing TEXT COMMENT '触发时机',
    terminal_types VARCHAR(255) COMMENT '支持端类型，逗号分隔',
    owner VARCHAR(128) COMMENT '负责人',
    is_core TINYINT DEFAULT 0 COMMENT '是否核心事件: 0-否, 1-是',
    status VARCHAR(64) NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT, REVIEWING, PUBLISHED, DEPRECATED',
    version INT DEFAULT 1 COMMENT '版本号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_event_code (event_code),
    INDEX idx_status (status),
    INDEX idx_business_domain (business_domain),
    INDEX idx_event_type (event_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件定义表';

-- 属性定义表
CREATE TABLE IF NOT EXISTS tracking_property (
    id BIGINT NOT NULL PRIMARY KEY,
    property_code VARCHAR(128) NOT NULL COMMENT '属性编码',
    property_name VARCHAR(255) NOT NULL COMMENT '属性名称',
    property_type VARCHAR(64) NOT NULL COMMENT '属性类型: EVENT, USER, DEVICE, COMMON',
    data_type VARCHAR(64) NOT NULL COMMENT '数据类型: STRING, NUMBER, BOOLEAN, DATE, DATETIME, ENUM, ARRAY, OBJECT',
    description TEXT COMMENT '描述',
    is_required TINYINT DEFAULT 0 COMMENT '是否默认必填: 0-否, 1-是',
    is_sensitive TINYINT DEFAULT 0 COMMENT '是否敏感: 0-否, 1-是',
    security_level VARCHAR(64) COMMENT '安全等级',
    enum_values TEXT COMMENT '枚举值 JSON 数组',
    max_length INT COMMENT '最大长度',
    validation_rule TEXT COMMENT '校验规则',
    standard_code VARCHAR(128) COMMENT '关联数据标准编码',
    status VARCHAR(64) NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT, REVIEWING, PUBLISHED, DEPRECATED',
    version INT DEFAULT 1 COMMENT '版本号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_property_code (property_code),
    INDEX idx_status (status),
    INDEX idx_property_type (property_type),
    INDEX idx_data_type (data_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性定义表';

-- 事件属性关系表
CREATE TABLE IF NOT EXISTS tracking_event_property (
    id BIGINT NOT NULL PRIMARY KEY,
    event_id BIGINT NOT NULL COMMENT '事件ID',
    property_id BIGINT NOT NULL COMMENT '属性ID',
    is_required TINYINT DEFAULT 0 COMMENT '在该事件中是否必填: 0-否, 1-是',
    default_value VARCHAR(512) COMMENT '默认值',
    sample_value VARCHAR(512) COMMENT '样例值',
    description TEXT COMMENT '在该事件中的说明',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_event_property (event_id, property_id),
    INDEX idx_event_id (event_id),
    INDEX idx_property_id (property_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件属性关系表';

-- 埋点需求表
CREATE TABLE IF NOT EXISTS tracking_demand (
    id BIGINT NOT NULL PRIMARY KEY,
    demand_code VARCHAR(128) NOT NULL COMMENT '需求编号',
    demand_name VARCHAR(255) NOT NULL COMMENT '需求名称',
    business_domain VARCHAR(128) COMMENT '业务域',
    product_line VARCHAR(128) COMMENT '产品线',
    terminal_types VARCHAR(255) COMMENT '涉及端类型，逗号分隔',
    priority VARCHAR(64) COMMENT '优先级: P0, P1, P2, P3',
    business_goal TEXT COMMENT '业务目标',
    analysis_goal TEXT COMMENT '分析目标',
    product_owner VARCHAR(128) COMMENT '产品负责人',
    tech_owner VARCHAR(128) COMMENT '技术负责人',
    test_owner VARCHAR(128) COMMENT '测试负责人',
    data_owner VARCHAR(128) COMMENT '数据负责人',
    expected_release_date DATE COMMENT '期望上线日期',
    status VARCHAR(64) NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT, DESIGNING, REVIEWING, DEVELOPING, ACCEPTING, RELEASING, ONLINE, CLOSED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_demand_code (demand_code),
    INDEX idx_status (status),
    INDEX idx_business_domain (business_domain),
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点需求表';

-- 埋点方案表
CREATE TABLE IF NOT EXISTS tracking_plan (
    id BIGINT NOT NULL PRIMARY KEY,
    plan_code VARCHAR(128) NOT NULL COMMENT '方案编号',
    plan_name VARCHAR(255) NOT NULL COMMENT '方案名称',
    demand_id BIGINT COMMENT '关联需求ID',
    version INT DEFAULT 1 COMMENT '方案版本',
    description TEXT COMMENT '方案描述',
    status VARCHAR(64) NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT, REVIEWING, DEVELOPING, ACCEPTING, RELEASING, PUBLISHED, CLOSED',
    reviewer VARCHAR(128) COMMENT '评审人',
    published_version_id BIGINT COMMENT '已发布版本ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_plan_code (plan_code),
    INDEX idx_status (status),
    INDEX idx_demand_id (demand_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点方案表';

-- 接入应用表
CREATE TABLE IF NOT EXISTS tracking_app (
    id BIGINT NOT NULL PRIMARY KEY,
    app_code VARCHAR(128) NOT NULL COMMENT '应用编码',
    app_name VARCHAR(255) NOT NULL COMMENT '应用名称',
    app_type VARCHAR(64) COMMENT '应用类型: WEB, IOS, ANDROID, MINI_PROGRAM, SERVER',
    description TEXT COMMENT '描述',
    secret_key VARCHAR(255) COMMENT '密钥',
    report_url VARCHAR(512) COMMENT '上报地址',
    status VARCHAR(64) NOT NULL DEFAULT 'ENABLED' COMMENT '状态: ENABLED, DISABLED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_app_code (app_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接入应用表';

-- 上报事件样本表
CREATE TABLE IF NOT EXISTS tracking_event_sample (
    id BIGINT NOT NULL PRIMARY KEY,
    app_code VARCHAR(128) NOT NULL COMMENT '应用编码',
    debug_token VARCHAR(128) COMMENT 'Debug Token',
    event_code VARCHAR(128) NOT NULL COMMENT '事件编码',
    event_time DATETIME NOT NULL COMMENT '事件发生时间',
    ingestion_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '服务端接收时间',
    terminal_type VARCHAR(64) COMMENT '端类型: WEB, IOS, ANDROID, MINI_PROGRAM, SERVER',
    environment VARCHAR(64) COMMENT '环境: DEV, TEST, PRE, PROD',
    user_id VARCHAR(128) COMMENT '用户ID',
    anonymous_id VARCHAR(128) COMMENT '匿名ID',
    session_id VARCHAR(128) COMMENT '会话ID',
    device_id VARCHAR(128) COMMENT '设备ID',
    sdk_version VARCHAR(64) COMMENT 'SDK版本',
    app_version VARCHAR(64) COMMENT '应用版本',
    page_code VARCHAR(128) COMMENT '页面编码',
    request_id VARCHAR(128) COMMENT '请求ID',
    payload LONGTEXT COMMENT '原始JSON payload',
    validate_status VARCHAR(64) COMMENT '校验状态: PASS, WARN, FAIL, UNKNOWN',
    validate_errors TEXT COMMENT '校验错误JSON',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_app_code (app_code),
    INDEX idx_event_code (event_code),
    INDEX idx_debug_token (debug_token),
    INDEX idx_user_id (user_id),
    INDEX idx_event_time (event_time),
    INDEX idx_environment (environment)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上报事件样本表';

-- 方案事件关系表
CREATE TABLE IF NOT EXISTS tracking_plan_event (
    id BIGINT NOT NULL PRIMARY KEY,
    plan_id BIGINT NOT NULL COMMENT '方案ID',
    event_id BIGINT NOT NULL COMMENT '事件ID',
    is_required TINYINT DEFAULT 0 COMMENT '在该方案中是否必填: 0-否, 1-是',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    UNIQUE KEY uk_plan_event (plan_id, event_id),
    INDEX idx_plan_id (plan_id),
    INDEX idx_event_id (event_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='方案事件关系表';

-- Debug 会话表
CREATE TABLE IF NOT EXISTS tracking_debug_session (
    id BIGINT NOT NULL PRIMARY KEY,
    debug_token VARCHAR(128) NOT NULL COMMENT 'Debug Token',
    app_code VARCHAR(128) COMMENT '应用编码',
    user_id VARCHAR(128) COMMENT '用户ID',
    anonymous_id VARCHAR(128) COMMENT '匿名ID',
    device_id VARCHAR(128) COMMENT '设备ID',
    environment VARCHAR(64) COMMENT '环境',
    expired_at DATETIME COMMENT '过期时间',
    status VARCHAR(64) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, EXPIRED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_debug_token (debug_token),
    INDEX idx_status (status),
    INDEX idx_app_code (app_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Debug会话表';

-- 埋点验收任务表
CREATE TABLE IF NOT EXISTS tracking_acceptance_task (
    id BIGINT NOT NULL PRIMARY KEY,
    task_code VARCHAR(128) NOT NULL COMMENT '验收任务编号',
    plan_id BIGINT NOT NULL COMMENT '方案ID',
    debug_token VARCHAR(128) NOT NULL COMMENT 'Debug Token',
    environment VARCHAR(64) COMMENT '环境',
    status VARCHAR(64) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, RUNNING, PASS, FAIL',
    event_coverage_rate DECIMAL(10,4) COMMENT '事件覆盖率',
    required_property_complete_rate DECIMAL(10,4) COMMENT '必填属性完整率',
    type_valid_rate DECIMAL(10,4) COMMENT '类型正确率',
    result_summary TEXT COMMENT '结果摘要JSON',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_task_code (task_code),
    INDEX idx_plan_id (plan_id),
    INDEX idx_debug_token (debug_token),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点验收任务表';

-- 埋点验收结果表
CREATE TABLE IF NOT EXISTS tracking_acceptance_result (
    id BIGINT NOT NULL PRIMARY KEY,
    task_id BIGINT NOT NULL COMMENT '验收任务ID',
    event_id BIGINT COMMENT '事件ID',
    event_code VARCHAR(128) COMMENT '事件编码',
    status VARCHAR(64) NOT NULL COMMENT 'PASS, FAIL',
    error_items TEXT COMMENT '错误项JSON',
    sample_ids TEXT COMMENT '命中的样本ID JSON',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_task_id (task_id),
    INDEX idx_event_code (event_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点验收结果表';

-- 埋点发布版本表
CREATE TABLE IF NOT EXISTS tracking_release (
    id BIGINT NOT NULL PRIMARY KEY,
    release_code VARCHAR(128) NOT NULL COMMENT '发布编号',
    plan_id BIGINT NOT NULL COMMENT '方案ID',
    version INT NOT NULL COMMENT '发布版本',
    status VARCHAR(64) NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT, SUBMITTED, PUBLISHED, CANCELED',
    diff_summary TEXT COMMENT '变更摘要JSON',
    published_at DATETIME COMMENT '发布时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_release_code (release_code),
    INDEX idx_plan_id (plan_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点发布版本表';

-- 埋点发布版本明细表
CREATE TABLE IF NOT EXISTS tracking_release_item (
    id BIGINT NOT NULL PRIMARY KEY,
    release_id BIGINT NOT NULL COMMENT '发布ID',
    item_type VARCHAR(64) NOT NULL COMMENT 'EVENT, PROPERTY, PLAN',
    item_id BIGINT NOT NULL COMMENT '对象ID',
    item_code VARCHAR(128) COMMENT '对象编码',
    change_type VARCHAR(64) COMMENT 'ADD, UPDATE, DELETE',
    snapshot TEXT COMMENT '发布快照JSON',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_release_id (release_id),
    INDEX idx_item_type (item_type),
    INDEX idx_item_code (item_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点发布版本明细表';

-- 埋点质量指标表
CREATE TABLE IF NOT EXISTS tracking_quality_metric (
    id BIGINT NOT NULL PRIMARY KEY,
    app_code VARCHAR(128) COMMENT '应用编码',
    event_code VARCHAR(128) COMMENT '事件编码',
    environment VARCHAR(64) COMMENT '环境',
    metric_time DATETIME NOT NULL COMMENT '统计时间',
    metric_granularity VARCHAR(64) NOT NULL COMMENT 'MINUTE, HOUR, DAY',
    total_count BIGINT DEFAULT 0 COMMENT '总上报量',
    pass_count BIGINT DEFAULT 0 COMMENT '通过量',
    warn_count BIGINT DEFAULT 0 COMMENT '警告量',
    fail_count BIGINT DEFAULT 0 COMMENT '失败量',
    pass_rate DECIMAL(10,4) COMMENT '通过率',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_event_time (event_code, metric_time),
    INDEX idx_app_time (app_code, metric_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点质量指标表';

-- 埋点质量告警表
CREATE TABLE IF NOT EXISTS tracking_alert (
    id BIGINT NOT NULL PRIMARY KEY,
    alert_type VARCHAR(64) NOT NULL COMMENT 'NO_DATA, FAIL_RATE_HIGH, PROPERTY_MISSING',
    app_code VARCHAR(128) COMMENT '应用编码',
    event_code VARCHAR(128) COMMENT '事件编码',
    alert_level VARCHAR(64) COMMENT 'INFO, WARN, ERROR',
    alert_message TEXT COMMENT '告警信息',
    status VARCHAR(64) NOT NULL DEFAULT 'OPEN' COMMENT 'OPEN, CLOSED',
    triggered_at DATETIME COMMENT '触发时间',
    closed_at DATETIME COMMENT '关闭时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_status (status),
    INDEX idx_event_code (event_code),
    INDEX idx_triggered_at (triggered_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='埋点质量告警表';

-- 质量规则配置表
CREATE TABLE IF NOT EXISTS tracking_quality_rule (
    id BIGINT NOT NULL PRIMARY KEY,
    rule_code VARCHAR(128) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(255) NOT NULL COMMENT '规则名称',
    event_code VARCHAR(128) COMMENT '为空表示全局规则',
    app_code VARCHAR(128) COMMENT '为空表示全局规则',
    alert_type VARCHAR(64) NOT NULL COMMENT 'NO_DATA, FAIL_RATE_HIGH, PROPERTY_MISSING',
    threshold_value DECIMAL(10,4) COMMENT '阈值：失败率用小数，断流用小时数',
    time_window_minutes INT DEFAULT 60 COMMENT '时间窗口（分钟）',
    alert_level VARCHAR(64) NOT NULL COMMENT 'INFO, WARN, ERROR',
    notify_targets TEXT COMMENT '通知对象 JSON',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用: 0-否, 1-是',
    is_core_event_only TINYINT DEFAULT 0 COMMENT '是否仅核心事件: 0-否, 1-是',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_rule_code (rule_code),
    INDEX idx_event_code (event_code),
    INDEX idx_app_code (app_code),
    INDEX idx_enabled (is_enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质量规则配置表';

-- ============================================
-- 平台自身 module_click 事件和属性初始化数据
-- ============================================

-- module_click 事件
INSERT INTO tracking_event (id, event_code, event_name, event_type, business_domain, description, trigger_timing, terminal_types, owner, is_core, status, version, created_by, updated_by)
VALUES (1000001, 'module_click', '模块点击', 'CLICK', 'platform', '平台模块点击事件，用于统计各模块的访问热度', '用户点击平台左侧菜单、顶部导航或首页模块卡片时触发', 'WEB', 'system', 1, 'PUBLISHED', 1, 'system', 'system')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- module_click 属性
INSERT INTO tracking_property (id, property_code, property_name, property_type, data_type, description, is_required, is_sensitive, status, version, created_by, updated_by)
VALUES
(1000001, 'module_code', '模块编码', 'EVENT', 'STRING', '模块稳定编码，如 data_assets、data_collection', 1, 0, 'PUBLISHED', 1, 'system', 'system'),
(1000002, 'module_name', '模块名称', 'EVENT', 'STRING', '模块名称，如 数据资产、数据采集', 1, 0, 'PUBLISHED', 1, 'system', 'system'),
(1000003, 'parent_module_code', '父模块编码', 'EVENT', 'STRING', '父模块编码，如一级菜单编码', 0, 0, 'PUBLISHED', 1, 'system', 'system'),
(1000004, 'route_path', '路由路径', 'EVENT', 'STRING', '点击后跳转的路由路径，如 /data-assets', 1, 0, 'PUBLISHED', 1, 'system', 'system'),
(1000005, 'click_position', '点击位置', 'EVENT', 'STRING', '点击位置，如 sidebar_menu、top_nav、home_card', 0, 0, 'PUBLISHED', 1, 'system', 'system'),
(1000006, 'source_page', '来源页面', 'EVENT', 'STRING', '点击来源页面编码', 0, 0, 'PUBLISHED', 1, 'system', 'system')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- module_click 事件属性绑定
INSERT INTO tracking_event_property (id, event_id, property_id, is_required, description, created_by, updated_by)
VALUES
(1000001, 1000001, 1000001, 1, '模块编码', 'system', 'system'),
(1000002, 1000001, 1000002, 1, '模块名称', 'system', 'system'),
(1000003, 1000001, 1000003, 0, '父模块编码', 'system', 'system'),
(1000004, 1000001, 1000004, 1, '路由路径', 'system', 'system'),
(1000005, 1000001, 1000005, 0, '点击位置', 'system', 'system'),
(1000006, 1000001, 1000006, 0, '来源页面', 'system', 'system')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- 属性维度映射表
CREATE TABLE IF NOT EXISTS tracking_property_dimension_mapping (
    id BIGINT NOT NULL PRIMARY KEY,
    property_id BIGINT NOT NULL COMMENT '属性ID',
    property_code VARCHAR(128) NOT NULL COMMENT '属性编码',
    dim_id VARCHAR(64) COMMENT '指标平台维度ID',
    dim_code VARCHAR(128) NOT NULL COMMENT '指标平台维度编码',
    sync_status VARCHAR(32) NOT NULL COMMENT '同步状态: PENDING,SUCCESS,FAILED',
    error_message VARCHAR(1024) COMMENT '错误信息',
    created_by VARCHAR(64),
    updated_by VARCHAR(64),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    UNIQUE KEY uk_property_id (property_id),
    UNIQUE KEY uk_dim_code (dim_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集属性维度映射表';

-- 事件指标映射表
CREATE TABLE IF NOT EXISTS tracking_event_metric_mapping (
    id BIGINT NOT NULL PRIMARY KEY,
    event_id BIGINT NOT NULL COMMENT '事件ID',
    event_code VARCHAR(128) NOT NULL COMMENT '事件编码',
    metric_id VARCHAR(64) COMMENT '指标平台指标ID',
    metric_code VARCHAR(128) NOT NULL COMMENT '指标平台指标编码',
    sync_status VARCHAR(32) NOT NULL COMMENT '同步状态: PENDING,SUCCESS,FAILED',
    error_message VARCHAR(1024) COMMENT '错误信息',
    created_by VARCHAR(64),
    updated_by VARCHAR(64),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    UNIQUE KEY uk_event_id (event_id),
    UNIQUE KEY uk_metric_code (metric_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集事件指标映射表';


-- 采集指标链路表
CREATE TABLE IF NOT EXISTS tracking_metric_pipeline (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    metric_code VARCHAR(128) NOT NULL COMMENT '指标编码',
    metric_name VARCHAR(255) NOT NULL COMMENT '指标名称',
    event_code VARCHAR(128) NOT NULL COMMENT '来源事件编码',
    app_code VARCHAR(128) COMMENT '应用编码',
    dimensions_json TEXT COMMENT '维度字段JSON',
    measures_json TEXT COMMENT '指标度量JSON',
    topic_name VARCHAR(255) NOT NULL COMMENT 'Kafka Topic',
    ods_table_name VARCHAR(255) NOT NULL COMMENT 'ODS表',
    dwd_table_name VARCHAR(255) NOT NULL COMMENT 'DWD表',
    dws_table_name VARCHAR(255) NOT NULL COMMENT 'DWS表',
    ads_table_name VARCHAR(255) NOT NULL COMMENT 'ADS表',
    dataworks_job_id VARCHAR(128) COMMENT 'DataWorks作业ID',
    dataworks_instance_id VARCHAR(128) COMMENT 'DataWorks实例ID',
    flink_deployment_name VARCHAR(255) COMMENT 'FlinkDeployment名称',
    status VARCHAR(64) NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT,TABLE_CREATED,JOB_CREATED,RUNNING,FAILED',
    error_message TEXT COMMENT '错误信息',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    UNIQUE KEY uk_metric_code (metric_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集指标链路表';
