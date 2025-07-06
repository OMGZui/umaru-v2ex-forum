import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { topicService, nodeService } from '../services/api';

const CreateTopic = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuth();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    nodeId: ''
  });
  const [nodes, setNodes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    // 检查用户是否已登录
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    // 获取节点列表
    const fetchNodes = async () => {
      try {
        const response = await nodeService.getAllNodes();
        setNodes(response.data);
      } catch (err) {
        console.error('Error fetching nodes:', err);
        setError('获取节点列表失败');
      }
    };

    fetchNodes();
  }, [isAuthenticated, navigate]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // 表单验证
    if (!formData.title.trim()) {
      setError('请输入主题标题');
      return;
    }
    
    if (formData.title.length < 5) {
      setError('标题至少需要5个字符');
      return;
    }
    
    if (formData.title.length > 200) {
      setError('标题不能超过200个字符');
      return;
    }
    
    if (!formData.nodeId) {
      setError('请选择一个节点');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await topicService.createTopic({
        title: formData.title.trim(),
        content: formData.content.trim(),
        nodeId: parseInt(formData.nodeId)
      });

      // 创建成功，跳转到主题详情页
      navigate(`/topic/${response.data.id}`);
    } catch (err) {
      console.error('Error creating topic:', err);
      if (err.response && err.response.status === 401) {
        setError('请先登录');
        navigate('/login');
      } else {
        setError(err.message || '发布主题失败，请重试');
      }
    } finally {
      setLoading(false);
    }
  };

  if (!isAuthenticated) {
    return (
      <main className="main">
        <div className="content">
          <div className="topic-list">
            <div style={{ padding: '20px', textAlign: 'center' }}>
              请先登录
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
            <h2 style={{ marginBottom: '20px', fontSize: '18px', fontWeight: 'normal' }}>
              创建新主题
            </h2>
            
            {error && (
              <div style={{
                width: '100%',
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

            <form onSubmit={handleSubmit} style={{ width: '100%' }}>
              <div style={{ marginBottom: '15px' }}>
                <label style={{ 
                  display: 'block', 
                  marginBottom: '5px', 
                  fontSize: '14px',
                  color: '#333'
                }}>
                  选择节点 *
                </label>
                <select
                  name="nodeId"
                  value={formData.nodeId}
                  onChange={handleInputChange}
                  required
                  style={{
                    width: '200px',
                    padding: '8px',
                    border: '1px solid #ddd',
                    borderRadius: '3px',
                    fontSize: '14px'
                  }}
                >
                  <option value="">请选择节点</option>
                  {nodes.map(node => (
                    <option key={node.id} value={node.id}>
                      {node.name}
                    </option>
                  ))}
                </select>
              </div>

              <div style={{ marginBottom: '15px' }}>
                <label style={{ 
                  display: 'block', 
                  marginBottom: '5px', 
                  fontSize: '14px',
                  color: '#333'
                }}>
                  主题标题 *
                </label>
                <input
                  type="text"
                  name="title"
                  value={formData.title}
                  onChange={handleInputChange}
                  placeholder="请输入主题标题（5-200个字符）"
                  required
                  maxLength={200}
                  style={{
                    width: '100%',
                    padding: '10px',
                    border: '1px solid #ddd',
                    borderRadius: '3px',
                    fontSize: '14px',
                    boxSizing: 'border-box'
                  }}
                />
                <div style={{ 
                  fontSize: '12px', 
                  color: '#666', 
                  marginTop: '5px' 
                }}>
                  {formData.title.length}/200
                </div>
              </div>

              <div style={{ marginBottom: '20px' }}>
                <label style={{ 
                  display: 'block', 
                  marginBottom: '5px', 
                  fontSize: '14px',
                  color: '#333'
                }}>
                  主题内容
                </label>
                <textarea
                  name="content"
                  value={formData.content}
                  onChange={handleInputChange}
                  placeholder="请输入主题内容（可选）"
                  rows={8}
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
              </div>

              <div style={{ display: 'flex', gap: '10px' }}>
                <button
                  type="submit"
                  disabled={loading}
                  style={{
                    padding: '10px 20px',
                    backgroundColor: loading ? '#ccc' : '#4CAF50',
                    color: 'white',
                    border: 'none',
                    borderRadius: '3px',
                    fontSize: '14px',
                    cursor: loading ? 'not-allowed' : 'pointer'
                  }}
                >
                  {loading ? '发布中...' : '发布主题'}
                </button>
                
                <button
                  type="button"
                  onClick={() => navigate('/')}
                  style={{
                    padding: '10px 20px',
                    backgroundColor: '#f5f5f5',
                    color: '#333',
                    border: '1px solid #ddd',
                    borderRadius: '3px',
                    fontSize: '14px',
                    cursor: 'pointer'
                  }}
                >
                  取消
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </main>
  );
};

export default CreateTopic;
