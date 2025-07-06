import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { topicService } from '../services/api';
import ReplyList from './ReplyList';
import useAvatarCache from '../hooks/useAvatarCache';

const TopicDetail = () => {
  const { id } = useParams();
  const { user, isAuthenticated } = useAuth();
  const { getAvatarUrl } = useAvatarCache();
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
                src={getAvatarUrl(topic.author.id, topic.author.avatar || 'https://i.imgur.com/8QmIp.png')}
                alt={`${topic.author.username}的头像`}
                className="avatar"
              />
              <div className="topic-content" style={{ flex: 1 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <h1 className="topic-title" style={{ fontSize: '20px', marginBottom: '8px' }}>
                    {topic.title}
                  </h1>
                  {isAuthenticated && user && topic.author.id === user.id && (
                    <Link
                      to={`/edit-topic/${topic.id}`}
                      style={{
                        padding: '6px 12px',
                        backgroundColor: '#f5f5f5',
                        color: '#333',
                        border: '1px solid #ddd',
                        borderRadius: '3px',
                        fontSize: '12px',
                        textDecoration: 'none',
                        marginLeft: '10px'
                      }}
                    >
                      编辑
                    </Link>
                  )}
                </div>
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

            {/* 回复列表和回复表单 */}
            <ReplyList topicId={id} />
          </div>
        </div>
      </div>
    </main>
  );
};

export default TopicDetail;
