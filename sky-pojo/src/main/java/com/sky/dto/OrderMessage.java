@Data
@Builder
public class OrderMessage {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private Integer status; // 消息状态：0-待处理，1-处理成功，2-处理失败
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}