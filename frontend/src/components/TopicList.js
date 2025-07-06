import { useNavigate } from 'react-router-dom';
import useAvatarCache from '../hooks/useAvatarCache';

const TopicList = ({ topics }) => {
  const navigate = useNavigate();
  const { getAvatarUrl } = useAvatarCache();

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

  if (!topics || topics.length === 0) {
    return (
      <div className="topic-list">
        <div style={{
          padding: '40px 20px',
          textAlign: 'center',
          color: '#666',
          fontSize: '16px'
        }}>
          <div style={{ marginBottom: '10px' }}>🌟</div>
          <div>暂无主题</div>
          <div style={{ fontSize: '14px', marginTop: '8px' }}>
            成为第一个发布主题的人吧！
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="topic-list">
      {topics.map((topic) => (
        <div key={topic.id} className="topic-item">
          <img
            src={getAvatarUrl(topic.author.id, topic.author.avatar || 'https://i.imgur.com/8QmIp.png')}
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
