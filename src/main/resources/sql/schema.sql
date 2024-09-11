CREATE TABLE IF NOT EXISTS users
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE KEY,
    name        VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders
(
    id               BIGINT                             Not NULL COMMENT '订单id' PRIMARY KEY,
    user_id          BIGINT                             Not NULL COMMENT '订单所属人',
    serve_type_id    BIGINT                             NULL COMMENT '服务类型id',
    serve_type_name  VARCHAR(50)                        NULL COMMENT '服务类型名称',
    serve_item_id    BIGINT                             Not NULL COMMENT '服务项id',
    serve_item_name  VARCHAR(50)                        NULL COMMENT '服务项名称',
    serve_item_img   VARCHAR(255)                       NULL COMMENT '服务项图片',
    unit             VARCHAR(50)                        NULL COMMENT '单位',
    serve_id         BIGINT                             NOT NULL COMMENT '服务id',
    orders_status    INT                                NOT NULL COMMENT '订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：已取消，700：已关闭',
    pay_status       INT                                NULL COMMENT '支付状态，2：待支付，4：支付成功',
    refund_status    INT                                NULL COMMENT '退款状态 1退款中 2退款成功 3退款失败',
    price            DECIMAL(10, 2)                     NOT NULL COMMENT '单价',
    pur_num          INT      DEFAULT 1                 NOT NULL COMMENT '购买数量',
    total_amount     DECIMAL(10, 2)                     NOT NULL COMMENT '总金额',
    real_pay_amount  DECIMAL(10, 2)                     NOT NULL COMMENT '实付金额',
    discount_amount  DECIMAL(10, 2)                     NOT NULL COMMENT '优惠金额',
    city_code        VARCHAR(20)                        NOT NULL COMMENT '城市编码',
    serve_address    VARCHAR(255)                       NOT NULL COMMENT '服务地址',
    contacts_phone   VARCHAR(20)                        NOT NULL COMMENT '联系人电话',
    contacts_name    VARCHAR(20)                        NOT NULL COMMENT '联系人姓名',
    serve_start_time DATETIME                           NOT NULL COMMENT '服务开始时间',
    lon              DOUBLE(10, 5)                      NULL COMMENT '经度',
    lat              DOUBLE(10, 5)                      NULL COMMENT '纬度',
    pay_time         DATETIME                           NULL COMMENT '支付时间',
    evaluation_time  DATETIME                           NULL COMMENT '评价时间',
    trading_order_no BIGINT                             NULL COMMENT '支付服务订单号',
    transaction_id   VARCHAR(50)                        NULL COMMENT '第三方支付的订单号',
    refund_no        BIGINT                             NULL COMMENT '退款单号',
    refund_id        VARCHAR(50)                        NULL COMMENT '第三方支付的退款单号',
    trading_channel  VARCHAR(50)                        NULL COMMENT '支付渠道',
    display          INT      DEFAULT 1                 NULL COMMENT '用户端是否展示，1：展示，0：隐藏',
    sort_by          BIGINT                             NULL COMMENT '排序字段，serve_start_time毫秒级时间戳+订单id后六位',
    create_time      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
) COMMENT '订单表' CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS orders_archive
(
    id               BIGINT NOT NULL COMMENT '订单id' PRIMARY KEY,
    access_count     BIGINT   DEFAULT 0 COMMENT '访问次数',
    last_access_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
    storage_type     VARCHAR(10) COMMENT '存储类型（HOT / COLD）'
) COMMENT '订单归档表' CHARSET = utf8mb4;