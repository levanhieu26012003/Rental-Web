        package com.lvh.RentalBE.controllers;

        import com.lvh.RentalBE.dto.UserDTO;
        import com.lvh.RentalBE.model.User;
        import com.lvh.RentalBE.services.FollowService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.Collection;
        import java.util.Map;
        import java.util.stream.Collectors;

        @RestController
        @RequestMapping("/api/follow")
        public class FollowController {

            @Autowired
            private FollowService followService;

            @PostMapping(path="/follow",consumes = MediaType.APPLICATION_JSON_VALUE)
            public ResponseEntity<String> followUser(@RequestBody Map<String, String> params) {
                followService.followUser(Long.parseLong(params.get("followerId")), Long.parseLong(params.get("hostId")));

                return ResponseEntity.ok("Followed successfully");
            }

            @PostMapping("/unfollow")
            public ResponseEntity<String> unfollowUser(@RequestBody Map<String, String> params) {
                followService.unfollowUser(Long.parseLong(params.get("followerId")), Long.parseLong(params.get("hostId")));
                return ResponseEntity.ok("Unfollowed successfully");
            }

            @GetMapping("/followers/{userId}")
            public ResponseEntity<Collection<UserDTO>> followers(@PathVariable Long userId) {
                Collection<User> followers = followService.Followers(userId);
                Collection<UserDTO> followerDTOs = followers.stream()
                        .map(user -> new UserDTO(user.getId(), user.getUsername(),user.getFullname(), user.getEmail(),user.getUserRole(),user.getAvatar(),user.getActive()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(followerDTOs);
            }

            @GetMapping("/hosts/{userId}")
            public ResponseEntity<Collection<UserDTO>> hosts(@PathVariable Long userId) {
                Collection<User> hosts = followService.Hosts(userId);
                Collection<UserDTO> followerDTOs = hosts.stream()
                        .map(user -> new UserDTO(user.getId(), user.getUsername(),user.getFullname(), user.getEmail(),user.getUserRole(),user.getAvatar(), user.getActive()))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(followerDTOs);
            }

            @GetMapping("/countFollowers/{userId}")
            public ResponseEntity<Integer> followersCount(@PathVariable Long userId) {
                return ResponseEntity.ok(this.followService.countByFollowersUserId(userId));
            }

        //    @GetMapping("/hosts/{userId}")
        //    public ResponseEntity<Map<String>> hosts(@PathVariable Long userId) {
        //        Long count = followService.Hosts(userId);
        //        return ResponseEntity.ok(count);
        //    }
        }
