import { useState, useEffect } from 'react';
import { nodeService, statsService } from '../services/api';
import useAvatarCache from '../hooks/useAvatarCache';

const Sidebar = () => {
  const [hotNodes, setHotNodes] = useState([]);
  const [stats, setStats] = useState({
    totalMembers: 0,
    totalTopics: 0,
    totalReplies: 0,
    todayTopics: 0,
    todayReplies: 0,
  });
  const [statsLoading, setStatsLoading] = useState(true);
  const [activeMembers, setActiveMembers] = useState([]);
  const [activeMembersLoading, setActiveMembersLoading] = useState(true);

  // 使用头像缓存Hook
  const { processActiveMembersAvatars, batchPreloadAvatars } = useAvatarCache();

  useEffect(() => {
    const fetchNodes = async () => {
      try {
        const response = await nodeService.getAllNodes();
        // 取前8个节点作为热门节点
        setHotNodes(response.data.slice(0, 8));
      } catch (error) {
        console.error('Error fetching nodes:', error);
        // 如果获取失败，使用默认数据
        setHotNodes([
          { name: '技术', slug: 'tech' },
          { name: '分享创造', slug: 'share' },
          { name: 'Apple', slug: 'apple' },
          { name: '问与答', slug: 'qna' },
          { name: '创业', slug: 'startup' },
        ]);
      }
    };

    fetchNodes();
  }, []);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setStatsLoading(true);
        const response = await statsService.getCommunityStats();
        setStats({
          totalMembers: response.data.totalMembers,
          totalTopics: response.data.totalTopics,
          totalReplies: response.data.totalReplies,
          todayTopics: response.data.todayTopics,
          todayReplies: response.data.todayReplies,
        });
      } catch (error) {
        console.error('Error fetching stats:', error);
        // 如果获取失败，使用默认数据
        setStats({
          totalMembers: 123456,
          totalTopics: 234567,
          totalReplies: 1234567,
          todayTopics: 89,
          todayReplies: 456,
        });
      } finally {
        setStatsLoading(false);
      }
    };

    fetchStats();
  }, []);

  useEffect(() => {
    const fetchActiveMembers = async () => {
      try {
        setActiveMembersLoading(true);
        const response = await statsService.getActiveMembers();

        // 使用头像缓存处理活跃会员数据
        const membersWithCache = processActiveMembersAvatars(response.data);
        setActiveMembers(membersWithCache);

        // 预加载头像（异步进行，不阻塞UI）
        if (response.data && response.data.length > 0) {
          const avatarList = response.data.map(member => ({
            userId: member.id,
            avatarUrl: member.avatar
          })).filter(item => item.avatarUrl);

          batchPreloadAvatars(avatarList).then(successCount => {
            if (successCount > 0) {
              console.log(`Successfully preloaded ${successCount} avatars`);
            }
          });
        }
      } catch (error) {
        console.error('Error fetching active members:', error);
        // 如果获取失败，使用默认数据
        const defaultMembers = [
          {
            id: 1,
            username: '小埋',
            avatar: 'https://i.imgur.com/8QmIp.png',
            activityScore: 100
          },
          {
            id: 2,
            username: '用户2',
            avatar: 'https://i.imgur.com/6VBx3.png',
            activityScore: 80
          },
          {
            id: 3,
            username: '用户3',
            avatar: 'https://i.imgur.com/kH25Y.png',
            activityScore: 60
          }
        ];

        // 对默认数据也应用缓存
        const membersWithCache = processActiveMembersAvatars(defaultMembers);
        setActiveMembers(membersWithCache);
      } finally {
        setActiveMembersLoading(false);
      }
    };

    fetchActiveMembers();
  }, [processActiveMembersAvatars, batchPreloadAvatars]);

  // 格式化数字显示
  const formatNumber = (num) => {
    if (num >= 1000000) {
      return (num / 1000000).toFixed(1) + 'M';
    } else if (num >= 1000) {
      return (num / 1000).toFixed(1) + 'K';
    }
    return num.toLocaleString();
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-box">
        <div className="sidebar-header">热门节点</div>
        <div className="sidebar-content">
          <div className="node-list">
            {hotNodes.length > 0 ? (
              hotNodes.map((node) => (
                <a
                  key={node.slug}
                  href={`/node/${node.slug}`}
                  className="node-tag"
                >
                  {node.name}
                </a>
              ))
            ) : (
              <div style={{
                padding: '20px',
                textAlign: 'center',
                color: '#999',
                fontSize: '14px'
              }}>
                暂无节点
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="sidebar-box">
        <div className="sidebar-header">社区状态</div>
        <div className="sidebar-content">
          {statsLoading ? (
            <div style={{ padding: '20px', textAlign: 'center', color: '#999' }}>
              加载中...
            </div>
          ) : (
            <>
              <div className="stats-item">
                <span>注册会员</span>
                <span>{formatNumber(stats.totalMembers)}</span>
              </div>
              <div className="stats-item">
                <span>主题总数</span>
                <span>{formatNumber(stats.totalTopics)}</span>
              </div>
              <div className="stats-item">
                <span>回复总数</span>
                <span>{formatNumber(stats.totalReplies)}</span>
              </div>
              <div className="stats-item">
                <span>今日主题</span>
                <span>{formatNumber(stats.todayTopics)}</span>
              </div>
              <div className="stats-item">
                <span>今日回复</span>
                <span>{formatNumber(stats.todayReplies)}</span>
              </div>
            </>
          )}
        </div>
      </div>

      <div className="sidebar-box">
        <div className="sidebar-header">最近活跃会员</div>
        <div className="sidebar-content">
          {activeMembersLoading ? (
            <div style={{ padding: '20px', textAlign: 'center', color: '#999' }}>
              加载中...
            </div>
          ) : (
            <div className="active-members">
              {activeMembers.length > 0 ? (
                activeMembers.map((member) => (
                  <div
                    key={member.id}
                    className="member-item"
                    title={`${member.username} (活跃度: ${member.activityScore})`}
                  >
                    <img
                      src={member.avatar || 'https://i.imgur.com/8QmIp.png'}
                      alt={member.username}
                      className="member-avatar"
                    />
                    <span className="member-name">{member.username}</span>
                  </div>
                ))
              ) : (
                <div style={{
                  padding: '20px',
                  textAlign: 'center',
                  color: '#999',
                  fontSize: '14px'
                }}>
                  暂无活跃会员
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
