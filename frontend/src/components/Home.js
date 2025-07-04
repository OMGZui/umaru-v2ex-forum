import React, { useState, useEffect } from 'react';
import { topicService } from '../services/api';
import TopicList from './TopicList';
import Sidebar from './Sidebar';

const Home = () => {
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTopics = async () => {
      try {
        setLoading(true);
        const response = await topicService.getRecentTopics();
        setTopics(response.data);
        setError(null);
      } catch (err) {
        console.error('Error fetching topics:', err);
        setError('获取主题列表失败');
      } finally {
        setLoading(false);
      }
    };

    fetchTopics();
  }, []);

  if (loading) {
    return (
      <main className="main">
        <div className="content">
          <div className="topic-list">
            <div style={{ padding: '20px', textAlign: 'center' }}>
              加载中...
            </div>
          </div>
        </div>
        <Sidebar />
      </main>
    );
  }

  if (error) {
    return (
      <main className="main">
        <div className="content">
          <div className="topic-list">
            <div style={{ padding: '20px', textAlign: 'center', color: 'red' }}>
              {error}
            </div>
          </div>
        </div>
        <Sidebar />
      </main>
    );
  }

  return (
    <main className="main">
      <div className="content">
        <TopicList topics={topics} />
      </div>
      <Sidebar />
    </main>
  );
};

export default Home;
