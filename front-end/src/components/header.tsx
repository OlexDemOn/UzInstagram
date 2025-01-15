import { Link, useNavigate } from "@tanstack/react-router";
import React from "react";
import { twMerge } from "tailwind-merge";
import { LanguageToggle, ModeToggle } from "./mode-toggle";
import { useAuth } from "../../providers/AuthProvider";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { FaNetworkWired } from "react-icons/fa";
import { useTranslation } from "react-i18next";

const Header = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    return (
        <header
            className={twMerge(
                "flex items-center justify-between sticky w-full z-50 bg-background top-0 h-16 p-4",
                props.className
            )}
            ref={ref}
            {...props}
        >
            <Logo />
            <div className="absolute left-1/2 transform -translate-x-1/2">
                <Navigation />
            </div>
            <div className="flex gap-x-4 w-30">
                <ModeToggle />
                <LanguageToggle />
                <User />
            </div>
        </header>
    );
});

const Logo = React.forwardRef<HTMLDivElement, React.HTMLProps<HTMLDivElement>>(
    (props, ref) => {
        return (
            <div
                className={twMerge("flex items-center w-40", props.className)}
                ref={ref}
                {...props}
            >
                <span className="font-semibold flex items-center gap-x-2">
                    <FaNetworkWired size={30} />
                    Social Network
                </span>
            </div>
        );
    }
);

const Navigation = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    const { t } = useTranslation();

    const menuItems = [
        { label: t("menu_main"), href: "/" },
        { label: t("menu_create"), href: "/createPost" },
        { label: t("menu_save"), href: "/saved" },
        { label: t("Top ten"), href: "/topTen" },
    ];

    return (
        <nav
            className={twMerge(
                "flex items-center gap-x-3 justify-center",
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
        const { t } = useTranslation();

        const handleLogout = () => {
            auth.logout();
            navigate("/");
            window.location.reload();
        };

        return (
            <div
                className={twMerge("flex items-center", props.className)}
                ref={ref}
                {...props}
            >
                <DropdownMenu>
                    <DropdownMenuTrigger className="hover:text-primary transition-all">
                        {t("profile")}
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                        <DropdownMenuLabel>{t("username")} </DropdownMenuLabel>
                        <Link to={`/profile/${auth.user?.username}`}>
                            <DropdownMenuItem className="hover:cursor-pointer">
                                {auth.user?.username}
                            </DropdownMenuItem>
                        </Link>
                        <Link to="/profileinfo">
                            <DropdownMenuItem className="hover:cursor-pointer">
                                {t("settings")}
                            </DropdownMenuItem>
                        </Link>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem
                            onClick={handleLogout}
                            className="hover:cursor-pointer"
                        >
                            {t("logout")}
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
        );
    }
);

export default Header;
