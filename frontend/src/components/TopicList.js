import React from 'react';
import { useNavigate } from 'react-router-dom';

const TopicList = ({ topics }) => {
  const navigate = useNavigate();

  const handleTopicClick = (topicId) => {
    navigate(`/topic/${topicId}`);
  };

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

  return (
    <div className="topic-list">
      {topics.map((topic) => (
        <div key={topic.id} className="topic-item">
          <img 
            src={topic.author.avatar || 'https://i.imgur.com/8QmIp.png'} 
            alt={`${topic.author.username}的头像`} 
            className="avatar"
          />
          <div className="topic-content">
            <div 
              className="topic-title"
              onClick={() => handleTopicClick(topic.id)}
            >
              {topic.title}
            </div>
            <div className="topic-meta">
              <a href={`/node/${topic.node.slug}`}>{topic.node.name}</a> • 
              <a href={`/user/${topic.author.username}`}> {topic.author.username}</a> • 
              {formatTimeAgo(topic.createdAt)}
            </div>
          </div>
          <div className="topic-stats">
            <span className="reply-count">{topic.replyCount}</span>
          </div>
        </div>
      ))}
    </div>
  );
};

export default TopicList;
