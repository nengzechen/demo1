package actors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Actor消息基类
 * 定义Actor之间传递的消息类型
 */
public interface TaskMessage extends Serializable {

    /**
     * 处理任务消息
     */
    class ProcessTask implements TaskMessage {
        private final String taskId;
        private final String taskName;
        private final String taskData;
        private final LocalDateTime createdAt;

        public ProcessTask(String taskId, String taskName, String taskData) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.taskData = taskData;
            this.createdAt = LocalDateTime.now();
        }

        public String getTaskId() {
            return taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public String getTaskData() {
            return taskData;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public String toString() {
            return "ProcessTask{" +
                    "taskId='" + taskId + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", createdAt=" + createdAt +
                    '}';
        }
    }

    /**
     * 定时任务触发消息
     */
    class ScheduledTick implements TaskMessage {
        private final LocalDateTime tickTime;

        public ScheduledTick() {
            this.tickTime = LocalDateTime.now();
        }

        public LocalDateTime getTickTime() {
            return tickTime;
        }

        @Override
        public String toString() {
            return "ScheduledTick{tickTime=" + tickTime + '}';
        }
    }

    /**
     * 任务完成消息
     */
    class TaskCompleted implements TaskMessage {
        private final String taskId;
        private final boolean success;
        private final String result;
        private final LocalDateTime completedAt;

        public TaskCompleted(String taskId, boolean success, String result) {
            this.taskId = taskId;
            this.success = success;
            this.result = result;
            this.completedAt = LocalDateTime.now();
        }

        public String getTaskId() {
            return taskId;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getResult() {
            return result;
        }

        public LocalDateTime getCompletedAt() {
            return completedAt;
        }

        @Override
        public String toString() {
            return "TaskCompleted{" +
                    "taskId='" + taskId + '\'' +
                    ", success=" + success +
                    ", completedAt=" + completedAt +
                    '}';
        }
    }

    /**
     * 查询任务状态消息
     */
    class QueryTaskStatus implements TaskMessage {
        private final String taskId;

        public QueryTaskStatus(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskId() {
            return taskId;
        }

        @Override
        public String toString() {
            return "QueryTaskStatus{taskId='" + taskId + '\'' + '}';
        }
    }

    /**
     * 任务状态响应消息
     */
    class TaskStatusResponse implements TaskMessage {
        private final String taskId;
        private final String status;
        private final String message;

        public TaskStatusResponse(String taskId, String status, String message) {
            this.taskId = taskId;
            this.status = status;
            this.message = message;
        }

        public String getTaskId() {
            return taskId;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "TaskStatusResponse{" +
                    "taskId='" + taskId + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
