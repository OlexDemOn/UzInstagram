import React, { useEffect, useState } from 'react';
import Post from './post.tsx'; // Імпорт компоненту для відображення одного поста


type TPost = {
  id: number;
  title: string;
  body: string;
};

const PostList: React.FC = () => {
  const [posts, setPosts] = useState<TPost[]>([]); 
  const [loading, setLoading] = useState<boolean>(true); 

  
  const fetchPosts = async () => {
    try {
      const response = await fetch('http://localhost:5173'); 
      const data = await response.json();
      setPosts(data); 
      setLoading(false); 
    } catch (error) {
      console.error('Error fetching posts:', error);
      setLoading(false); 
    }
  };

  
  useEffect(() => {
    fetchPosts();
  }, []);

  if (loading) {
    return <div>Loading...</div>; 
  }

  return (
    <div>
      {posts.length === 0 ? (
        <p>No posts found.</p> 
      ) : (
        <div>
          {posts.map((post) => (
            <Post key={post.id} post={post} /> 
          ))}
        </div>
      )}
    </div>
  );
};

export default PostList;
