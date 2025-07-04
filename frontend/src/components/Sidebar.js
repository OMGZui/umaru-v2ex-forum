import React from 'react';

const Sidebar = () => {
  const hotNodes = [
    { name: '程序员', slug: 'programmer' },
    { name: 'Python', slug: 'python' },
    { name: 'JavaScript', slug: 'javascript' },
    { name: 'Apple', slug: 'apple' },
    { name: '创业', slug: 'startup' },
    { name: '设计', slug: 'design' },
    { name: '生活', slug: 'life' },
    { name: '分享发现', slug: 'share' },
  ];

  const stats = {
    members: '123,456',
    topics: '234,567',
    replies: '1,234,567',
    todayTopics: '89',
    todayReplies: '456',
  };

  const activeMembers = [
    'https://i.imgur.com/8QmIp.png',
    'https://i.imgur.com/VQWPsBS.png',
    'https://i.imgur.com/kH25Y.png',
    'https://i.imgur.com/2QoLk.png',
    'https://i.imgur.com/9QmIp.png',
    'https://i.imgur.com/8QmIp.png',
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-box">
        <div className="sidebar-header">热门节点</div>
        <div className="sidebar-content">
          <div className="node-list">
            {hotNodes.map((node) => (
              <a 
                key={node.slug} 
                href={`/node/${node.slug}`} 
                className="node-tag"
              >
                {node.name}
              </a>
            ))}
          </div>
        </div>
      </div>

      <div className="sidebar-box">
        <div className="sidebar-header">社区状态</div>
        <div className="sidebar-content">
          <div className="stats-item">
            <span>注册会员</span>
            <span>{stats.members}</span>
          </div>
          <div className="stats-item">
            <span>主题总数</span>
            <span>{stats.topics}</span>
          </div>
          <div className="stats-item">
            <span>回复总数</span>
            <span>{stats.replies}</span>
          </div>
          <div className="stats-item">
            <span>今日主题</span>
            <span>{stats.todayTopics}</span>
          </div>
          <div className="stats-item">
            <span>今日回复</span>
            <span>{stats.todayReplies}</span>
          </div>
        </div>
      </div>

      <div className="sidebar-box">
        <div className="sidebar-header">最近活跃会员</div>
        <div className="sidebar-content">
          <div className="active-members">
            {activeMembers.map((avatar, index) => (
              <img 
                key={index}
                src={avatar} 
                alt="活跃会员" 
                className="member-avatar"
              />
            ))}
          </div>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
