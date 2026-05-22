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
