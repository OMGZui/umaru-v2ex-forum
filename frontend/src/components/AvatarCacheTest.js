import React, { useState, useEffect } from 'react';
import useAvatarCache from '../hooks/useAvatarCache';
import AvatarCacheUtil from '../utils/avatarCache';

/**
 * 头像缓存测试组件
 * 用于测试和演示头像缓存功能
 */
const AvatarCacheTest = () => {
  const {
    getAvatarUrl,
    setAvatarCache,
    removeAvatarCache,
    clearAllCache,
    clearExpiredCache,
    updateCacheStats,
    cacheStats
  } = useAvatarCache();

  const [testUserId, setTestUserId] = useState('test_user_1');
  const [testAvatarUrl, setTestAvatarUrl] = useState('https://i.imgur.com/8QmIp.png');
  const [cachedUrl, setCachedUrl] = useState('');
  const [logs, setLogs] = useState([]);

  // 添加日志
  const addLog = (message) => {
    const timestamp = new Date().toLocaleTimeString();
    setLogs(prev => [...prev, `[${timestamp}] ${message}`]);
  };

  // 测试设置缓存
  const testSetCache = () => {
    const success = setAvatarCache(testUserId, testAvatarUrl);
    addLog(`设置缓存: ${success ? '成功' : '失败'} - 用户ID: ${testUserId}`);
    updateCacheStats();
  };

  // 测试获取缓存
  const testGetCache = () => {
    const url = getAvatarUrl(testUserId, testAvatarUrl);
    setCachedUrl(url);
    addLog(`获取缓存: ${url ? '成功' : '失败'} - URL: ${url}`);
  };

  // 测试移除缓存
  const testRemoveCache = () => {
    const success = removeAvatarCache(testUserId);
    addLog(`移除缓存: ${success ? '成功' : '失败'} - 用户ID: ${testUserId}`);
    updateCacheStats();
  };

  // 测试清除所有缓存
  const testClearAllCache = () => {
    const count = clearAllCache();
    addLog(`清除所有缓存: 清除了 ${count} 个缓存项`);
    updateCacheStats();
  };

  // 测试清除过期缓存
  const testClearExpiredCache = () => {
    const count = clearExpiredCache();
    addLog(`清除过期缓存: 清除了 ${count} 个过期缓存项`);
    updateCacheStats();
  };

  // 测试批量设置缓存
  const testBatchSetCache = () => {
    const avatarList = [
      { userId: 'user_1', avatarUrl: 'https://i.imgur.com/8QmIp.png' },
      { userId: 'user_2', avatarUrl: 'https://i.imgur.com/6VBx3.png' },
      { userId: 'user_3', avatarUrl: 'https://i.imgur.com/kH25Y.png' }
    ];
    
    const count = AvatarCacheUtil.batchSetAvatars(avatarList);
    addLog(`批量设置缓存: 成功设置 ${count} 个头像缓存`);
    updateCacheStats();
  };

  // 清除日志
  const clearLogs = () => {
    setLogs([]);
  };

  // 组件挂载时更新统计信息
  useEffect(() => {
    updateCacheStats();
    addLog('头像缓存测试组件已加载');
  }, [updateCacheStats]);

  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
      <h2>头像缓存功能测试</h2>
      
      {/* 缓存统计信息 */}
      <div style={{ 
        background: '#f5f5f5', 
        padding: '15px', 
        borderRadius: '5px', 
        marginBottom: '20px' 
      }}>
        <h3>缓存统计信息</h3>
        {cacheStats ? (
          <div>
            <p>总缓存数量: {cacheStats.totalCount}</p>
            <p>有效缓存数量: {cacheStats.validCount}</p>
            <p>过期缓存数量: {cacheStats.expiredCount}</p>
            <p>缓存总大小: {cacheStats.totalSize} 字节</p>
            <p>缓存过期时间: {cacheStats.expiryHours} 小时</p>
          </div>
        ) : (
          <p>加载中...</p>
        )}
      </div>

      {/* 测试控制面板 */}
      <div style={{ 
        background: '#fff', 
        border: '1px solid #ddd', 
        padding: '15px', 
        borderRadius: '5px', 
        marginBottom: '20px' 
      }}>
        <h3>测试控制面板</h3>
        
        <div style={{ marginBottom: '10px' }}>
          <label>用户ID: </label>
          <input
            type="text"
            value={testUserId}
            onChange={(e) => setTestUserId(e.target.value)}
            style={{ marginLeft: '10px', padding: '5px' }}
          />
        </div>
        
        <div style={{ marginBottom: '15px' }}>
          <label>头像URL: </label>
          <input
            type="text"
            value={testAvatarUrl}
            onChange={(e) => setTestAvatarUrl(e.target.value)}
            style={{ marginLeft: '10px', padding: '5px', width: '300px' }}
          />
        </div>

        <div style={{ marginBottom: '15px' }}>
          <button onClick={testSetCache} style={{ marginRight: '10px' }}>
            设置缓存
          </button>
          <button onClick={testGetCache} style={{ marginRight: '10px' }}>
            获取缓存
          </button>
          <button onClick={testRemoveCache} style={{ marginRight: '10px' }}>
            移除缓存
          </button>
        </div>

        <div style={{ marginBottom: '15px' }}>
          <button onClick={testBatchSetCache} style={{ marginRight: '10px' }}>
            批量设置缓存
          </button>
          <button onClick={testClearExpiredCache} style={{ marginRight: '10px' }}>
            清除过期缓存
          </button>
          <button onClick={testClearAllCache} style={{ marginRight: '10px' }}>
            清除所有缓存
          </button>
        </div>

        {cachedUrl && (
          <div style={{ marginTop: '15px' }}>
            <h4>缓存的头像:</h4>
            <img 
              src={cachedUrl} 
              alt="缓存的头像" 
              style={{ width: '50px', height: '50px', borderRadius: '50%' }}
            />
            <p>URL: {cachedUrl}</p>
          </div>
        )}
      </div>

      {/* 日志显示 */}
      <div style={{ 
        background: '#f9f9f9', 
        border: '1px solid #ddd', 
        padding: '15px', 
        borderRadius: '5px' 
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h3>操作日志</h3>
          <button onClick={clearLogs}>清除日志</button>
        </div>
        
        <div style={{ 
          maxHeight: '300px', 
          overflowY: 'auto', 
          background: '#fff', 
          padding: '10px', 
          border: '1px solid #eee',
          fontFamily: 'monospace',
          fontSize: '12px'
        }}>
          {logs.length > 0 ? (
            logs.map((log, index) => (
              <div key={index} style={{ marginBottom: '5px' }}>
                {log}
              </div>
            ))
          ) : (
            <div style={{ color: '#999' }}>暂无日志</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AvatarCacheTest;
