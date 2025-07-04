import React from 'react';

const Header = () => {
  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <a href="/" className="logo">V2EX</a>
          <nav className="nav">
            <a href="/">首页</a>
            <a href="/nodes">节点</a>
            <a href="/timeline">时间线</a>
            <a href="/about">关于</a>
          </nav>
          <div className="user-actions">
            <a href="/login" className="btn">登录</a>
            <a href="/register" className="btn">注册</a>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
