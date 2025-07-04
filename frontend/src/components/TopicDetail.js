import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { topicService } from '../services/api';

const TopicDetail = () => {
  const { id } = useParams();
  const [topic, setTopic] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTopic = async () => {
      try {
        setLoading(true);
        const response = await topicService.getTopicById(id);
        setTopic(response.data);
        setError(null);
      } catch (err) {
        console.error('Error fetching topic:', err);
        setError('获取主题详情失败');
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchTopic();
    }
  }, [id]);

  const formatTimeAgo = (dateString) => {
    const now = new Date();
    const date = new Date(dateString);
    const diffInHours = Math.floor((now - date) / (1000 * 60 * 60));
    
    if (diffInHours < 1) {
      return '刚刚';
    } else if (diffInHours < 24) {
      return `${diffInHours} 小时前`;
    } else {
      const diffInDays = Math.floor(diffInHours / 24);
      return `${diffInDays} 天前`;
    }
  };

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
      </main>
    );
  }

  if (!topic) {
    return (
      <main className="main">
        <div className="content">
          <div className="topic-list">
            <div style={{ padding: '20px', textAlign: 'center' }}>
              主题不存在
            </div>
          </div>
        </div>
      </main>
    );
  }

  return (
    <main className="main">
      <div className="content">
        <div className="topic-list">
          <div className="topic-item" style={{ flexDirection: 'column', alignItems: 'flex-start' }}>
            <div style={{ display: 'flex', gap: '12px', width: '100%', marginBottom: '15px' }}>
              <img 
                src={topic.author.avatar || 'https://i.imgur.com/8QmIp.png'} 
                alt={`${topic.author.username}的头像`} 
                className="avatar"
              />
              <div className="topic-content">
                <h1 className="topic-title" style={{ fontSize: '20px', marginBottom: '8px' }}>
                  {topic.title}
                </h1>
                <div className="topic-meta">
                  <a href={`/node/${topic.node.slug}`}>{topic.node.name}</a> • 
                  <a href={`/user/${topic.author.username}`}> {topic.author.username}</a> • 
                  {formatTimeAgo(topic.createdAt)} • 
                  {topic.viewCount} 次点击
                </div>
              </div>
            </div>
            
            {topic.content && (
              <div style={{ 
                width: '100%', 
                padding: '15px', 
                backgroundColor: '#f8f8f8', 
                borderRadius: '3px',
                lineHeight: '1.6',
                whiteSpace: 'pre-wrap'
              }}>
                {topic.content}
              </div>
            )}
          </div>
        </div>
      </div>
    </main>
  );
};

export default TopicDetail;
