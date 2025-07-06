import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { replyService } from '../services/api';
import useAvatarCache from '../hooks/useAvatarCache';

const ReplyList = ({ topicId }) => {
  const { user, isAuthenticated } = useAuth();
  const { getAvatarUrl } = useAvatarCache();
  const [replies, setReplies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [replyContent, setReplyContent] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchReplies();
  }, [topicId]);

  const fetchReplies = async () => {
    try {
      setLoading(true);
      const response = await replyService.getRepliesByTopic(topicId);
      setReplies(response.data);
    } catch (err) {
      console.error('Error fetching replies:', err);
      setError('获取回复失败');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmitReply = async (e) => {
    e.preventDefault();
    
    if (!isAuthenticated) {
      setError('请先登录');
      return;
    }
    
    if (!replyContent.trim()) {
      setError('请输入回复内容');
      return;
    }
    
    if (replyContent.length > 1000) {
      setError('回复内容不能超过1000个字符');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      const response = await replyService.createReply({
        content: replyContent.trim(),
        topicId: parseInt(topicId)
      });

      // 添加新回复到列表
      setReplies(prev => [...prev, response.data]);
      setReplyContent('');
    } catch (err) {
      console.error('Error creating reply:', err);
      if (err.response && err.response.status === 401) {
        setError('请先登录');
      } else {
        setError(err.message || '发布回复失败，请重试');
      }
    } finally {
      setSubmitting(false);
    }
  };

  const formatTimeAgo = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffInSeconds = Math.floor((now - date) / 1000);

    if (diffInSeconds < 60) {
      return '刚刚';
    } else if (diffInSeconds < 3600) {
      return `${Math.floor(diffInSeconds / 60)} 分钟前`;
    } else if (diffInSeconds < 86400) {
      return `${Math.floor(diffInSeconds / 3600)} 小时前`;
    } else if (diffInSeconds < 2592000) {
      return `${Math.floor(diffInSeconds / 86400)} 天前`;
    } else {
      return date.toLocaleDateString();
    }
  };

  if (loading) {
    return (
      <div style={{ padding: '20px', textAlign: 'center' }}>
        加载回复中...
      </div>
    );
  }

  return (
    <div style={{ marginTop: '20px' }}>
      {/* 回复列表 */}
      {replies.length > 0 && (
        <div style={{ marginBottom: '20px' }}>
          <h3 style={{ 
            fontSize: '16px', 
            fontWeight: 'normal', 
            marginBottom: '15px',
            color: '#333'
          }}>
            {replies.length} 条回复
          </h3>
          
          {replies.map((reply, index) => (
            <div 
              key={reply.id} 
              className="topic-item" 
              style={{ 
                marginBottom: '10px',
                padding: '15px',
                borderLeft: index === 0 ? 'none' : '1px solid #e2e2e2'
              }}
            >
              <div style={{ display: 'flex', gap: '12px' }}>
                <img
                  src={getAvatarUrl(reply.author.id, reply.author.avatar || 'https://i.imgur.com/8QmIp.png')}
                  alt={`${reply.author.username}的头像`}
                  className="avatar"
                  style={{ width: '32px', height: '32px' }}
                />
                <div style={{ flex: 1 }}>
                  <div style={{ 
                    fontSize: '12px', 
                    color: '#666', 
                    marginBottom: '8px' 
                  }}>
                    <strong>{reply.author.username}</strong> · {formatTimeAgo(reply.createdAt)}
                  </div>
                  <div style={{ 
                    fontSize: '14px', 
                    lineHeight: '1.6',
                    color: '#333',
                    whiteSpace: 'pre-wrap'
                  }}>
                    {reply.content}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* 回复表单 */}
      {isAuthenticated ? (
        <div className="topic-item" style={{ marginTop: '20px' }}>
          <h3 style={{ 
            fontSize: '16px', 
            fontWeight: 'normal', 
            marginBottom: '15px',
            color: '#333'
          }}>
            添加回复
          </h3>
          
          {error && (
            <div style={{
              padding: '10px',
              marginBottom: '15px',
              backgroundColor: '#ffebee',
              border: '1px solid #f44336',
              borderRadius: '3px',
              color: '#d32f2f',
              fontSize: '14px'
            }}>
              {error}
            </div>
          )}

          <form onSubmit={handleSubmitReply}>
            <div style={{ marginBottom: '15px' }}>
              <textarea
                value={replyContent}
                onChange={(e) => setReplyContent(e.target.value)}
                placeholder="请输入回复内容..."
                rows={6}
                maxLength={1000}
                style={{
                  width: '100%',
                  padding: '10px',
                  border: '1px solid #ddd',
                  borderRadius: '3px',
                  fontSize: '14px',
                  resize: 'vertical',
                  boxSizing: 'border-box',
                  fontFamily: 'inherit'
                }}
              />
              <div style={{ 
                fontSize: '12px', 
                color: '#666', 
                marginTop: '5px',
                textAlign: 'right'
              }}>
                {replyContent.length}/1000
              </div>
            </div>

            <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
              <button
                type="submit"
                disabled={submitting || !replyContent.trim()}
                style={{
                  padding: '8px 16px',
                  backgroundColor: submitting || !replyContent.trim() ? '#ccc' : '#4CAF50',
                  color: 'white',
                  border: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  cursor: submitting || !replyContent.trim() ? 'not-allowed' : 'pointer'
                }}
              >
                {submitting ? '发布中...' : '发布回复'}
              </button>
              
              <span style={{ fontSize: '12px', color: '#666' }}>
                回复后不能编辑，请仔细检查内容
              </span>
            </div>
          </form>
        </div>
      ) : (
        <div className="topic-item" style={{ 
          marginTop: '20px', 
          textAlign: 'center',
          padding: '20px'
        }}>
          <p style={{ color: '#666', marginBottom: '10px' }}>
            请登录后参与讨论
          </p>
          <a 
            href="/login" 
            style={{
              padding: '8px 16px',
              backgroundColor: '#4CAF50',
              color: 'white',
              textDecoration: 'none',
              borderRadius: '3px',
              fontSize: '14px'
            }}
          >
            登录
          </a>
        </div>
      )}
    </div>
  );
};

export default ReplyList;
