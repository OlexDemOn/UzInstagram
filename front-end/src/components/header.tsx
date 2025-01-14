import { Link, useNavigate } from "@tanstack/react-router";
import React from "react";
import { twMerge } from "tailwind-merge";
import { ModeToggle } from "./mode-toggle";
import { useAuth } from "../../providers/AuthProvider";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

const menuItems = [
    { label: "Main", href: "/" },
    { label: "Create post", href: "/createPost" },
    // { label: "Logout", href: "/logout" },
];

const Header = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    return (
        <header
            className={twMerge(
                "flex items-center sticky w-full z-50 bg-background top-0 h-16 p-4 ",
                props.className
            )}
            ref={ref}
            {...props}
        >
            <Logo />
            <Navigation />
            <div className="flex gap-x-4">
                <ModeToggle />
                <User />
            </div>
        </header>
    );
});

const Logo = React.forwardRef<HTMLDivElement, React.HTMLProps<HTMLDivElement>>(
    (props, ref) => {
        return (
            <div
                className={twMerge("flex items-center", props.className)}
                ref={ref}
                {...props}
            >
                <span className="font-semibold">Logo</span>
            </div>
        );
    }
);

const Navigation = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    return (
        <nav
            className={twMerge(
                "flex flex-1 w-10 items-center gap-x-3 justify-center",
                props.className
            )}
            ref={ref}
            {...props}
        >
            {menuItems.map((item, index) => (
                <Link
                    className="hover:text-primary transition-all"
                    key={index}
                    to={item.href}
                >
                    {item.label}
                </Link>
            ))}
        </nav>
    );
});

const User = React.forwardRef<HTMLDivElement, React.HTMLProps<HTMLDivElement>>(
    (props, ref) => {
        const auth = useAuth();
        const navigate = useNavigate();

        const handleLogout = () => {
            auth.logout();
            navigate("/");
        };

        return (
            <div
                className={twMerge("flex items-center", props.className)}
                ref={ref}
                {...props}
            >
                <DropdownMenu>
                    <DropdownMenuTrigger className="hover:text-primary transition-all">
                        Profile
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                        <DropdownMenuLabel>Username: </DropdownMenuLabel>
                        <Link to="/profile">
                            <DropdownMenuItem className="hover:cursor-pointer">
                                {auth.user?.username}
                            </DropdownMenuItem>
                        </Link>
                        <Link to="/profileinfo">
                            <DropdownMenuItem className="hover:cursor-pointer">
                                Settings
                            </DropdownMenuItem>
                        </Link>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem
                            onClick={handleLogout}
                            className="hover:cursor-pointer"
                        >
                            Logout
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
        );
    }
);

export default Header;
