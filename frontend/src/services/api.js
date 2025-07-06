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

export default api;
