import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Link } from 'react-router-dom';
import useAvatarCache from '../hooks/useAvatarCache';

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const { getAvatarUrl } = useAvatarCache();

  const handleLogout = async () => {
    if (window.confirm('确定要退出登录吗？')) {
      try {
        await logout();
      } catch (error) {
        console.error('Logout failed:', error);
        // 即使logout失败，也显示成功消息，因为本地状态已经清理
        alert('已退出登录');
      }
    }
  };

  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <Link to="/" className="logo">🎮 小埋论坛</Link>
          <nav className="nav">
            <Link to="/">首页</Link>
            <Link to="/nodes">节点</Link>
            <Link to="/timeline">时间线</Link>
            <Link to="/about">关于</Link>
          </nav>
          <div className="user-actions">
            {isAuthenticated ? (
              <div className="user-menu">
                <Link to="/create-topic" className="btn create-topic-btn">
                  发布主题
                </Link>
                <img
                  src={getAvatarUrl(user.id, user.avatar || 'https://i.imgur.com/8QmIp.png')}
                  alt={user.username}
                  className="user-avatar"
                />
                <span className="username">{user.username}</span>
                <button onClick={handleLogout} className="btn logout-btn">
                  退出
                </button>
              </div>
            ) : (
              <>
                <Link to="/login" className="btn">登录</Link>
                <Link to="/register" className="btn">注册</Link>
              </>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
