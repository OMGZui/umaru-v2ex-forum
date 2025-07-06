import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 响应拦截器，处理统一的 ApiResponse 格式
api.interceptors.response.use(
  (response) => {
    // 如果响应数据有 code 字段，说明是 ApiResponse 格式
    if (response.data && typeof response.data.code !== 'undefined') {
      const { code, message, data } = response.data;

      // 检查业务状态码
      if (code === 200) {
        // 成功时返回实际数据
        return { ...response, data: data };
      } else {
        // 业务错误时抛出异常
        const error = new Error(message || '请求失败');
        error.code = code;
        error.response = response;
        throw error;
      }
    }

    // 如果不是 ApiResponse 格式，直接返回
    return response;
  },
  (error) => {
    // 处理 HTTP 错误
    if (error.response && error.response.data) {
      const { code, message } = error.response.data;
      if (message) {
        error.message = message;
      }
      if (code) {
        error.code = code;
      }
    }
    return Promise.reject(error);
  }
);

export const nodeService = {
  // 获取所有节点
  getAllNodes: () => {
    return api.get('/nodes');
  },

  // 根据slug获取节点
  getNodeBySlug: (slug) => {
    return api.get(`/nodes/${slug}`);
  },
};

export const topicService = {
  // 获取所有主题
  getAllTopics: (page = 0, size = 20) => {
    return api.get(`/topics?page=${page}&size=${size}`);
  },

  // 获取最近主题
  getRecentTopics: () => {
    return api.get('/topics/recent');
  },

  // 根据ID获取主题
  getTopicById: (id) => {
    return api.get(`/topics/${id}`);
  },

  // 创建新主题
  createTopic: (topicData) => {
    return api.post('/topics', topicData);
  },

  // 更新主题
  updateTopic: (id, topicData) => {
    return api.put(`/topics/${id}`, topicData);
  },
};

export const replyService = {
  // 获取主题的所有回复
  getRepliesByTopic: (topicId) => {
    return api.get(`/replies/topic/${topicId}`);
  },

  // 分页获取主题的回复
  getRepliesByTopicPaged: (topicId, page = 0, size = 20) => {
    return api.get(`/replies/topic/${topicId}/page?page=${page}&size=${size}`);
  },

  // 创建新回复
  createReply: (replyData) => {
    return api.post('/replies', replyData);
  },

  // 获取回复数量
  getReplyCountByTopic: (topicId) => {
    return api.get(`/replies/topic/${topicId}/count`);
  },

  // 获取用户的回复
  getRepliesByAuthor: (authorId, page = 0, size = 20) => {
    return api.get(`/replies/author/${authorId}?page=${page}&size=${size}`);
  },
};

export const userService = {
  // 获取当前用户信息
  getCurrentUser: () => {
    return api.get('/users/me');
  },

  // 根据用户名获取用户信息
  getUserByUsername: (username) => {
    return api.get(`/users/${username}`);
  },
};

export const statsService = {
  // 获取社区统计数据
  getCommunityStats: () => {
    return api.get('/stats/community');
  },

  // 获取注册会员总数
  getTotalMembers: () => {
    return api.get('/stats/members');
  },

  // 获取主题总数
  getTotalTopics: () => {
    return api.get('/stats/topics');
  },

  // 获取回复总数
  getTotalReplies: () => {
    return api.get('/stats/replies');
  },

  // 获取今日主题数
  getTodayTopics: () => {
    return api.get('/stats/today/topics');
  },

  // 获取今日回复数
  getTodayReplies: () => {
    return api.get('/stats/today/replies');
  },

  // 获取最近活跃会员
  getActiveMembers: () => {
    return api.get('/stats/active-members');
  },

  // 获取最近活跃会员（自定义参数）
  getActiveMembersCustom: (days = 7, limit = 6) => {
    return api.get(`/stats/active-members/custom?days=${days}&limit=${limit}`);
  },
};

export default api;
