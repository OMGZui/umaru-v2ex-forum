import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

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

export default api;
