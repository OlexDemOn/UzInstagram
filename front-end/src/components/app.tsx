import React from 'react';
import PostList from './PostList';

const App: React.FC = () => {
  return (
    <div className="App">
      <header>
        <h1>Posts</h1>
      </header>
      <PostList /> {/* Відображаємо всі пости */}
    </div>
  );
};

export default App;
